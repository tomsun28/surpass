// 扩展 Window 接口以支持旧版浏览器方法（可选）
declare global {
    interface Window {
        webkitRequestAnimationFrame?: (callback: FrameRequestCallback) => number;
        mozRequestAnimationFrame?: (callback: FrameRequestCallback) => number;
    }
}

export function easeInOutQuad(t: number, b: number, c: number, d: number): number {
    t /= d / 2;
    if (t < 1) return (c / 2) * t * t + b;
    t--;
    return (-c / 2) * (t * (t - 2) - 1) + b;
}

// requestAnimationFrame for Smart Animating http://goo.gl/sx5sts
let requestAnimFrame: any = (function () {
    return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || function (callback: any) {
        window.setTimeout(callback, 1000 / 60)
    }
})()

/**
 * Because it's so fucking difficult to detect the scrolling element, just move them all
 * @param {number} amount
 */
function move(amount: any): any {
    // 设置 documentElement.scrollTop（现代浏览器）
    if (document.documentElement) {
        document.documentElement.scrollTop = amount;
    }
    // 回退到 document.body.scrollTop（旧版浏览器）
    if (document.body) {
        document.body.scrollTop = amount;
    }
    document.body.scrollTop = amount
}

function position(): number {
    const scrollingElement = document.scrollingElement || document.documentElement;
    return scrollingElement.scrollTop || document.body?.scrollTop || 0;
}

/**
 * @param {number} to
 * @param {number} duration
 * @param {Function} callback
 */
export function scrollTo(to: any, duration: any, callback: any): any {
    const start: any = position()
    const change: any = to - start
    const increment: any = 20
    let currentTime: any = 0
    duration = (typeof (duration) === 'undefined') ? 500 : duration
    var animateScroll: any = function () {
        // increment the time
        currentTime += increment
        // find the value with the quadratic in-out easing function
        var val: any = easeInOutQuad(currentTime, start, change, duration)
        // move the document.body
        move(val)
        // do the animation unless its over
        if (currentTime < duration) {
            requestAnimFrame(animateScroll)
        } else {
            if (callback && typeof (callback) === 'function') {
                // the animation is done so lets callback
                callback()
            }
        }
    }
    animateScroll()
}
