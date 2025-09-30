/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_APP_BASE_API: string; // 你的环境变量
    // 其他环境变量声明...
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
