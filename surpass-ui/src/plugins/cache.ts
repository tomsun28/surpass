type StorageValue = string | null

interface Cache {
    set(key: string, value: string): void

    get(key: string): StorageValue

    setJSON<T>(key: string, jsonValue: T): void

    getJSON<T = unknown>(key: string): T | null

    remove(key: string): void
}

function createCache(storage: Storage | null): Cache {
    return {
        set(key: string, value: string) {
            if (!storage || !key || value == null) return
            storage.setItem(key, value)
        },

        get(key: string): StorageValue {
            if (!storage || !key) return null
            return storage.getItem(key)
        },

        setJSON<T>(key: string, jsonValue: T) {
            if (jsonValue != null) {
                this.set(key, JSON.stringify(jsonValue))
            }
        },

        getJSON<T = unknown>(key: string): T | null {
            const value = this.get(key)
            try {
                return value ? (JSON.parse(value) as T) : null
            } catch (err) {
                console.warn(`cache.getJSON(${key}) 解析失败:`, err)
                return null
            }
        },

        remove(key: string) {
            if (!storage || !key) return
            storage.removeItem(key)
        }
    }
}

const session = createCache(typeof sessionStorage !== 'undefined' ? sessionStorage : null)
const local = createCache(typeof localStorage !== 'undefined' ? localStorage : null)

export default {
    session,
    local
}
