import type { PropType as VuePropType, ComponentInternalInstance as ComponentInstance } from 'vue';

declare global {
  /** vue Instance */
  declare type ComponentInternalInstance = ComponentInstance;

  /**
   * 弹窗属性
   */
  declare interface DialogOption {
    /**
     * 弹窗标题
     */
    title?: string;
    /**
     * 是否显示
     */
    visible: boolean;
    type: number;
  }
}
export {};
