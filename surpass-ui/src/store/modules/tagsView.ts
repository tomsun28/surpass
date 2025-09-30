import {defineStore} from "pinia"

const useTagsViewStore: any = defineStore(
    'tags-view',
    {
        state: () => ({
            visitedViews: [],
            cachedViews: [],
            iframeViews: []
        }),
        actions: {
            addView(view: any) {
                this.addVisitedView(view)
                this.addCachedView(view)
            },
            addIframeView(view: any) {
                if (this.iframeViews.some((v: any) => v.path === view.path)) return
                this.iframeViews.push(
                    Object.assign({}, view, {
                        title: view.meta.title || 'no-name'
                    })
                )
            },
            addVisitedView(view: any) {
                if (this.visitedViews.some((v: any) => v.path === view.path)) return
                this.visitedViews.push(
                    Object.assign({}, view, {
                        title: view.meta.title || 'no-name'
                    })
                )
            },
            addCachedView(view: any) {
                if (this.cachedViews.includes(view.name)) return
                if (!view.meta.noCache) {
                    this.cachedViews.push(view.name)
                }
            },
            delView(view: any) {
                return new Promise((resolve: any) => {
                    this.delVisitedView(view)
                    this.delCachedView(view)
                    resolve({
                        visitedViews: [...this.visitedViews],
                        cachedViews: [...this.cachedViews]
                    })
                })
            },
            delVisitedView(view: any) {
                return new Promise((resolve: any) => {
                    for (const [i, v] of this.visitedViews.entries()) {
                        if (v.path === view.path) {
                            this.visitedViews.splice(i, 1)
                            break
                        }
                    }
                    this.iframeViews = this.iframeViews.filter((item: any) => item.path !== view.path)
                    resolve([...this.visitedViews])
                })
            },
            delIframeView(view: any) {
                return new Promise((resolve: any) => {
                    this.iframeViews = this.iframeViews.filter((item: any) => item.path !== view.path)
                    resolve([...this.iframeViews])
                })
            },
            delCachedView(view: any) {
                return new Promise((resolve: any) => {
                    const index: any = this.cachedViews.indexOf(view.name)
                    index > -1 && this.cachedViews.splice(index, 1)
                    resolve([...this.cachedViews])
                })
            },
            delOthersViews(view: any) {
                return new Promise((resolve: any) => {
                    this.delOthersVisitedViews(view)
                    this.delOthersCachedViews(view)
                    resolve({
                        visitedViews: [...this.visitedViews],
                        cachedViews: [...this.cachedViews]
                    })
                })
            },
            delOthersVisitedViews(view: any) {
                return new Promise((resolve: any) => {
                    this.visitedViews = this.visitedViews.filter((v: any) => {
                        return v.meta.affix || v.path === view.path
                    })
                    this.iframeViews = this.iframeViews.filter((item: any) => item.path === view.path)
                    resolve([...this.visitedViews])
                })
            },
            delOthersCachedViews(view: any) {
                return new Promise((resolve: any) => {
                    const index: any = this.cachedViews.indexOf(view.name)
                    if (index > -1) {
                        this.cachedViews = this.cachedViews.slice(index, index + 1)
                    } else {
                        this.cachedViews = []
                    }
                    resolve([...this.cachedViews])
                })
            },
            delAllViews(view: any) {
                return new Promise((resolve: any) => {
                    this.delAllVisitedViews(view)
                    this.delAllCachedViews(view)
                    resolve({
                        visitedViews: [...this.visitedViews],
                        cachedViews: [...this.cachedViews]
                    })
                })
            },
            delAllVisitedViews(view: any) {
                return new Promise((resolve: any) => {
                    const affixTags: any = this.visitedViews.filter((tag: any) => tag.meta.affix)
                    this.visitedViews = affixTags
                    this.iframeViews = []
                    resolve([...this.visitedViews])
                })
            },
            delAllCachedViews(view: any) {
                return new Promise((resolve: any) => {
                    this.cachedViews = []
                    resolve([...this.cachedViews])
                })
            },
            updateVisitedView(view: any) {
                for (let v of this.visitedViews) {
                    if (v.path === view.path) {
                        v = Object.assign(v, view)
                        break
                    }
                }
            },
            delRightTags(view: any) {
                return new Promise((resolve: any) => {
                    const index: any = this.visitedViews.findIndex((v: any) => v.path === view.path)
                    if (index === -1) {
                        return
                    }
                    this.visitedViews = this.visitedViews.filter((item: any, idx: any) => {
                        if (idx <= index || (item.meta && item.meta.affix)) {
                            return true
                        }
                        const i: any = this.cachedViews.indexOf(item.name)
                        if (i > -1) {
                            this.cachedViews.splice(i, 1)
                        }
                        if (item.meta.link) {
                            const fi: any = this.iframeViews.findIndex((v: any) => v.path === item.path)
                            this.iframeViews.splice(fi, 1)
                        }
                        return false
                    })
                    resolve([...this.visitedViews])
                })
            },
            delLeftTags(view: any) {
                return new Promise((resolve: any) => {
                    const index: any = this.visitedViews.findIndex((v: any) => v.path === view.path)
                    if (index === -1) {
                        return
                    }
                    this.visitedViews = this.visitedViews.filter((item: any, idx: any) => {
                        if (idx >= index || (item.meta && item.meta.affix)) {
                            return true
                        }
                        const i: any = this.cachedViews.indexOf(item.name)
                        if (i > -1) {
                            this.cachedViews.splice(i, 1)
                        }
                        if (item.meta.link) {
                            const fi: any = this.iframeViews.findIndex((v: any) => v.path === item.path)
                            this.iframeViews.splice(fi, 1)
                        }
                        return false
                    })
                    resolve([...this.visitedViews])
                })
            }
        }
    })

export default useTagsViewStore
