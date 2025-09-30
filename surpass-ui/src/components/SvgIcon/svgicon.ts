import * as components from '@element-plus/icons-vue'

export default {
    install: (app: any) => {
        for (const key in components) {
            const componentConfig: any = components[key];
            app.component(componentConfig.name, componentConfig);
        }
    },
};
