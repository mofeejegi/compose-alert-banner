package com.mofeejegi.alert

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.window.ComposeUIViewController
import com.mofeejegi.alert.ui.bannertype.AlertBannerType
import com.mofeejegi.alert.ui.composable.AlertBannerView
import com.mofeejegi.alert.ui.manager.AlertManager
import com.mofeejegi.alert.ui.state.AlertBannerViewModel
import com.mofeejegi.alert.ui.theme.AlertTheme
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGRect
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIEvent
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UISceneActivationStateForegroundInactive
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowLevelNormal
import platform.UIKit.UIWindowScene
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.willMoveToParentViewController

/**
 * iOS entry point for the AlertBanner library.
 *
 * ## SwiftUI / UIKit (window overlay)
 * ```swift
 * let alertBanner = AlertBannerIOS.shared
 *
 * // Optionally configure before attaching
 * alertBanner.configure(darkTheme: true)
 *
 * // Attach once at the root of your app. In a multi-window app, pass the scene
 * // the banners belong to with attach(scene:).
 * alertBanner.attach()
 *
 * // Show alerts from anywhere
 * alertBanner.showSuccess(message: "Saved!")
 * alertBanner.showError(message: "Something went wrong")
 * alertBanner.showCustom(message: "Synced", containerColor: .systemPurple, contentColor: .white)
 * ```
 *
 * ## UIKit (child view controller)
 * ```swift
 * class RootViewController: UIViewController {
 *     let alertBanner = AlertBannerIOS.shared
 *
 *     override func viewDidLoad() {
 *         super.viewDidLoad()
 *         // Install once on the root view controller
 *         alertBanner.install(on: self)
 *     }
 *
 *     @objc func onSave() {
 *         alertBanner.showSuccess(message: "Saved!")
 *     }
 * }
 * ```
 */
object AlertBannerIOS {
    private var darkTheme: Boolean? = null
    private var alertManager: AlertManager? = null
    private var overlayWindow: UIWindow? = null
    private var installedBannerVC: UIViewController? = null

    // Bounds of each visible banner, in points, relative to the overlay's Compose view. That view
    // fills the PassthroughWindow or PassthroughView exactly, so hitTest points can be compared
    // directly. Touches outside the banners pass through to the app. Empty when none are showing.
    private var bannerRects: List<Rect> = emptyList()

    @OptIn(ExperimentalForeignApi::class)
    private fun isBannerAt(point: CValue<CGPoint>): Boolean {
        val (x, y) = point.useContents { x to y }
        val offset = Offset(x.toFloat(), y.toFloat())
        return bannerRects.any { it.contains(offset) }
    }

    /**
     * Configures the alert banner theme. Call before [attach] or [install].
     *
     * @param darkTheme If true, uses dark theme. If false, uses light theme.
     *                  If not called, the system appearance is used.
     */
    fun configure(darkTheme: Boolean) {
        this.darkTheme = darkTheme
    }

    @OptIn(ExperimentalComposeUiApi::class)
    private fun makeViewController(): UIViewController {
        // Created here rather than inside the composition, so alerts shown right after
        // attach() or install() are kept and appear once the overlay first draws.
        val vm = AlertBannerViewModel()
        alertManager = AlertManager(vm::processEvent)

        val vc = ComposeUIViewController(
            configure = {
                opaque = false
                enforceStrictPlistSanityCheck = false
            }
        ) {
            AlertTheme(darkTheme = darkTheme ?: isSystemInDarkTheme()) {
                AlertBannerView(
                    vm = vm,
                    textStyle = AlertTheme.typography.bodySmall,
                    onAlertColor = AlertTheme.colorScheme.tone1,
                    usePopup = false,
                    onBannerBoundsChanged = { rectsPx ->
                        val scale = UIScreen.mainScreen.scale.toFloat()
                        bannerRects = rectsPx.map { rect ->
                            Rect(rect.left / scale, rect.top / scale, rect.right / scale, rect.bottom / scale)
                        }
                    },
                )
            }
        }
        vc.view.setBackgroundColor(UIColor.clearColor)
        return vc
    }

    fun showSuccess(message: String) {
        alertManager?.show(message, AlertBannerType.Success)
    }

    fun showError(message: String) {
        alertManager?.show(message, AlertBannerType.Error)
    }

    /**
     * Shows an alert in colors of your choosing: [containerColor] behind it and
     * [contentColor] for its icon and text. It shows the info icon.
     */
    fun showCustom(message: String, containerColor: UIColor, contentColor: UIColor) {
        showCustom(message, containerColor, contentColor, showIcon = true)
    }

    /**
     * Like [showCustom], with [showIcon] set to false to show the alert without an icon.
     */
    fun showCustom(message: String, containerColor: UIColor, contentColor: UIColor, showIcon: Boolean) {
        alertManager?.show(
            message,
            AlertBannerType.Custom(
                containerColor = containerColor.toComposeColor(),
                contentColor = contentColor.toComposeColor(),
                showIcon = showIcon,
            ),
        )
    }

    // -- Window overlay (works with SwiftUI and UIKit) --

    /**
     * Creates a transparent overlay window above the app's content and attaches
     * the alert banner to it. Works with both SwiftUI and UIKit.
     * Call once at the root of your app.
     *
     * Uses the app's active foreground scene. In a multi-window app, use
     * [attach] with a scene to choose which one shows the banners.
     */
    fun attach() {
        val scene = activeWindowScene() ?: return
        attach(scene)
    }

    /**
     * Like [attach], but shows the banners in the given [scene]. Calling it again
     * with a different scene moves the banners there.
     */
    @OptIn(ExperimentalForeignApi::class)
    fun attach(scene: UIWindowScene) {
        overlayWindow?.let { window ->
            if (window.windowScene != scene) window.windowScene = scene
            return
        }

        val window = PassthroughWindow(
            windowScene = scene,
            isBannerAt = ::isBannerAt,
        )
        window.windowLevel = UIWindowLevelNormal + 1
        window.rootViewController = makeViewController()
        window.setBackgroundColor(UIColor.clearColor)
        window.setHidden(false)

        overlayWindow = window
    }

    /**
     * Removes the overlay window.
     */
    fun detach() {
        overlayWindow?.setHidden(true)
        overlayWindow?.rootViewController = null
        overlayWindow = null
        alertManager = null
        bannerRects = emptyList()
    }

    private fun activeWindowScene(): UIWindowScene? {
        val scenes = UIApplication.sharedApplication.connectedScenes.filterIsInstance<UIWindowScene>()
        return scenes.firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
            ?: scenes.firstOrNull { it.activationState == UISceneActivationStateForegroundInactive }
            ?: scenes.firstOrNull()
    }

    // -- UIKit child view controller --

    /**
     * Installs the alert banner as a child overlay on the given parent UIViewController.
     * The banner view is pinned to all edges of the parent's view.
     * Call once on the root view controller.
     */
    @OptIn(ExperimentalForeignApi::class)
    fun install(on: UIViewController) {
        if (installedBannerVC != null) return

        val bannerVC = makeViewController()
        installedBannerVC = bannerVC

        val container = PassthroughView(
            frame = on.view.bounds,
            isBannerAt = ::isBannerAt,
        )
        container.setBackgroundColor(UIColor.clearColor)

        on.addChildViewController(bannerVC)
        container.addSubview(bannerVC.view)
        on.view.addSubview(container)

        container.setTranslatesAutoresizingMaskIntoConstraints(false)
        container.topAnchor.constraintEqualToAnchor(on.view.topAnchor).setActive(true)
        container.leadingAnchor.constraintEqualToAnchor(on.view.leadingAnchor).setActive(true)
        container.trailingAnchor.constraintEqualToAnchor(on.view.trailingAnchor).setActive(true)
        container.bottomAnchor.constraintEqualToAnchor(on.view.bottomAnchor).setActive(true)

        bannerVC.view.setTranslatesAutoresizingMaskIntoConstraints(false)
        bannerVC.view.topAnchor.constraintEqualToAnchor(container.topAnchor).setActive(true)
        bannerVC.view.leadingAnchor.constraintEqualToAnchor(container.leadingAnchor).setActive(true)
        bannerVC.view.trailingAnchor.constraintEqualToAnchor(container.trailingAnchor).setActive(true)
        bannerVC.view.bottomAnchor.constraintEqualToAnchor(container.bottomAnchor).setActive(true)

        bannerVC.didMoveToParentViewController(on)
    }

    /**
     * Removes the alert banner from its parent view controller.
     */
    fun uninstall() {
        installedBannerVC?.let { bannerVC ->
            bannerVC.willMoveToParentViewController(null)
            bannerVC.view.superview?.removeFromSuperview()
            bannerVC.removeFromParentViewController()
            installedBannerVC = null
            alertManager = null
            bannerRects = emptyList()
        }
    }

}

/**
 * A UIWindow that only intercepts touches on a visible alert banner.
 * All other touches pass through to the app below.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class PassthroughWindow(
    windowScene: UIWindowScene,
    private val isBannerAt: (CValue<CGPoint>) -> Boolean,
) : UIWindow(windowScene = windowScene) {

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        if (!isBannerAt(point)) return null
        return super.hitTest(point, withEvent)
    }
}

/**
 * A UIView that only intercepts touches on a visible alert banner.
 * All other touches pass through to views below.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class PassthroughView(
    frame: CValue<CGRect>,
    private val isBannerAt: (CValue<CGPoint>) -> Boolean,
) : UIView(frame = frame) {

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        if (!isBannerAt(point)) return null
        return super.hitTest(point, withEvent)
    }
}
