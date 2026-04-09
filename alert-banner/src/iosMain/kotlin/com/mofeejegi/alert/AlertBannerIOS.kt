package com.mofeejegi.alert

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
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
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowLevelNormal
import platform.UIKit.UISceneActivationStateForegroundActive
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
 * // Attach once at the root of your app
 * alertBanner.attach()
 *
 * // Show alerts from anywhere
 * alertBanner.showSuccess(message: "Saved!")
 * alertBanner.showError(message: "Something went wrong")
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

    // Actual bottom Y of the alert area in points, measured from Compose via onGloballyPositioned.
    // When 0, no alerts are showing and all touches pass through.
    private var alertAreaBottom = 0.0

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
        val vc = ComposeUIViewController(
            configure = { opaque = false }
        ) {
            val vm = viewModel { AlertBannerViewModel() }
            val manager = remember { AlertManager(vm::processEvent) }
            LaunchedEffect(manager) { alertManager = manager }

            // Reset touch area when all alerts are dismissed
            val viewState by vm.viewState.collectAsState()
            if (viewState.orderedAlerts.isEmpty()) alertAreaBottom = 0.0

            AlertTheme(darkTheme = darkTheme ?: isSystemInDarkTheme()) {
                AlertBannerView(
                    vm = vm,
                    textStyle = AlertTheme.typography.bodySmall,
                    onAlertColor = AlertTheme.colorScheme.tone1,
                    onAlertAreaChanged = { bottomPx ->
                        val density = platform.UIKit.UIScreen.mainScreen.scale
                        alertAreaBottom = bottomPx.toDouble() / density
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

    // -- Window overlay (works with SwiftUI and UIKit) --

    /**
     * Creates a transparent overlay window above the app's content and attaches
     * the alert banner to it. Works with both SwiftUI and UIKit.
     * Call once at the root of your app.
     */
    fun attach() {
        if (overlayWindow != null) return

        val scene = UIApplication.sharedApplication.connectedScenes
            .firstOrNull {
                it is UIWindowScene &&
                    it.activationState == UISceneActivationStateForegroundActive
            } as? UIWindowScene ?: return

        val window = PassthroughWindow(
            windowScene = scene,
            alertAreaHeight = { alertAreaBottom },
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
        alertAreaBottom = 0.0
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
            alertAreaHeight = { alertAreaBottom },
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
            alertAreaBottom = 0.0
        }
    }

}

/**
 * A UIWindow that only intercepts touches in the alert banner area at the top
 * of the screen. All other touches pass through to the app below.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class PassthroughWindow(
    windowScene: UIWindowScene,
    private val alertAreaHeight: () -> Double,
) : UIWindow(windowScene = windowScene) {

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        val height = alertAreaHeight()
        if (height <= 0.0) return null

        val y = point.useContents { y }
        if (y > height) return null

        return super.hitTest(point, withEvent)
    }
}

/**
 * A UIView that only intercepts touches in the alert banner area at the top.
 * All other touches pass through to views below.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class PassthroughView(
    frame: CValue<CGRect>,
    private val alertAreaHeight: () -> Double,
) : UIView(frame = frame) {

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        val height = alertAreaHeight()
        if (height <= 0.0) return null

        val y = point.useContents { y }
        if (y > height) return null

        return super.hitTest(point, withEvent)
    }
}
