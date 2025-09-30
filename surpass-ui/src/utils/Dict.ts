import useDictStore from '@/store/modules/dict'
import distData from '@/utils/DistData'
import {Ref, ref, toRefs} from "vue";
import type {DictItem} from '@/types/dict'

/**
 * 获取字典数据
 */
interface DictRefs {
    [key: string]: Ref<DictItem[]>;
}


export function useDict(...args: string[]): DictRefs {
    const res: any = ref({});
    return (() => {
        args.forEach((dictType: string) => {
            res.value[dictType] = [];
            const dicts: any = useDictStore().getDict(dictType);
            if (dicts) {
                res.value[dictType] = dicts;
            } else {
                res.value[dictType] = distData[dictType];
                useDictStore().setDict(dictType, res.value[dictType]);
            }
        });
        return toRefs(res.value);
    })();
}

