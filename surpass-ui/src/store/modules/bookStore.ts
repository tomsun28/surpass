import {defineStore} from "pinia";
import {listStore} from "@/api/system/book/book";
import {setSetList} from "@/utils/Auth";
import {getConfigKey, getBooksConfigList} from "@/api/config/sys";

const bookStore: any = defineStore(
    'books',
    {
        state: () => ({
            initializeTask: false,
            bookId: "",
            termCurrent: undefined,
            termStart: undefined,
            setList: [],
            // 科目编码规则[4,2,2,2]
            subjectCodeLen: [],
            // 初始化状态
            initializeStatus: false,
        }),
        actions: {
            updateBookId(id: any) {
                this.bookId = id;
            },
            getBookItem() {
                for (let i = 0; i < this.setList.length; i++) {
                    const item: any = this.setList[i]
                    if (item.id === this.bookId) {
                        return this.setList[i]
                    }
                }
            },
            refreshData() {
                return listStore().then((res: any) => {
                    this.setList = res.data;
                    setSetList(this.setList);

                    if (!this.bookId && this.setList.length > 0) {
                        const item: any = this.setList[0];
                        this.bookId = item.id;
                    }

                    return getBooksConfigList().then((res: any) => {
                        res.data.forEach((config: any) => {
                            const configValue = config.configValue;
                            if (config.configKey === "sys.subject.codes.length") {
                                const codes = (configValue || "4,2,2,2").split(',');
                                this.subjectCodeLen = codes.map((t: string) => parseInt(t));
                            } else if (config.configKey === "sys.initialize.task") {
                                if (configValue === 'true') {
                                    this.initializeStatus = true;
                                }
                                this.initializeTask = configValue;
                            } else if (config.configKey === "bookId") {
                                this.bookId = configValue;
                            } else if (config.configKey === "sys.payment.term.current") {
                                this.termCurrent = configValue;
                            } else if (config.configKey === "sys.payment.term.start") {
                                this.termStart = configValue;
                            }
                        });
                    });
                });
            }
        }
    }
)

export default bookStore
