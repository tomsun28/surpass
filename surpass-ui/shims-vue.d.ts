import modal from "@/plugins/modal";

declare module '@vue/runtime-core' {
    interface ComponentCustomProperties {
        $modal: typeof modal;
        // useDict: (key: string, ...args: string[]) => Record<string, any>;
    }
}

interface ImportMeta {
    glob: (pattern: string, options?: { eager?: boolean; import?: boolean; }) => Record<string, () => Promise<unknown>>;
}

interface ImportMeta {
    env: any;
}

interface Navigator {
    browserLanguage?: string;
}

declare module '*.vue' {
    import {DefineComponent} from 'vue';
    const component: DefineComponent<{}, {}, any>;
}

declare module '*.png' {
    const value: string;
}

declare module '*.module.scss' {
    const content: { [className: string]: string };
}

declare module '*.json' {
    const value: any;
    export default value;
}