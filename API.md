# API 说明

## 窗体

### BaseDialog

|  方法   | 说明  |
|  ----  | ----  |
| disableCancel  | 禁止按返回键取消窗体 |
| setBackgroundColor  | 设置窗体背景色（`CornerRound`可指定圆角类型） |
| setBackgroundResource  | 设置窗体背景 |
| setBackgroundDrawable  | 设置窗体背景 |
| setLayout  | 设置窗体宽高 |
| setWidth  | 设置窗体宽 |
| setHeight  | 设置窗体高 |
| setGravity  | 设置窗体对齐方式（居中、居底） |
| setDimAmount  | 设置窗体蒙层透明度 |
| setAnimationStyle  | 设置窗体动画 |
| setOnShowListener  | 设置窗体显示监听器 |
| setOnDismissListener  | 设置窗体关闭监听器 |

### ModalDialog

|  方法   | 说明  |
|  ----  | ----  |
| setTitle  | 设置窗体标题 |
| setBodyWidth  | 设置窗体主体内容宽度 |
| setBodyHeight  | 设置窗体主体内容高度 |
| getHeaderView  | 设置窗体头部内容视图 |
| getTopLineView  | 设置窗体顶部分割线视图 |
| getBodyView  | 设置窗体主体内容视图 |
| getFooterView  | 设置窗体脚部内容视图 |
| getCancelView  | 设置窗体取消按钮视图 |
| getTitleView  | 设置窗体标题视图 |
| getOkView  | 设置窗体确定按钮视图 |

### DialogConfig

|  方法   | 说明  |
|  ----  | ----  |
| setDialogStyle  | 设置全局的窗体样式（`DialogStyle`内置了四种样式） |
| getDialogStyle  | 获取已设置的全局窗体样式 |
| setDialogColor  | 设置全局的窗体配色（`DialogColor`） |
| getDialogColor  | 获取已设置的全局窗体配色 |

### DialogColor

|  方法   | 说明  |
|  ----  | ----  |
| contentBackgroundColor  | 全局的窗体内容背景色 |
| topLineColor  | 全局的窗体顶部分隔线颜色 |
| titleTextColor  | 全局的窗体标题文字颜色 |
| cancelTextColor  | 全局的窗体取消按钮文字颜色 |
| okTextColor  | 全局的窗体确定按钮文字颜色 |
| cancelEllipseColor  | 全局的窗体取消按钮椭圆颜色 |
| okEllipseColor  | 全局的窗体确定按钮椭圆颜色 |

## 滚轮

### WheelView/WheelLayout

|  方法   | 说明  |
|  ----  | ----  |
| scrollTo  | 滚动到指定位置 |
| setStyle  | 通过XML属性设置样式 |
| setAtmosphericEnabled  | 设置是否启用透明度渐变效果 |
| setCurtainEnabled  | 设置是否启用选中条目的背景 |
| setCurtainColor  | 设置选中条目的背景色 |
| setCurtainCorner  | 设置选中条目的圆角类型 |
| setCurtainRadius  | 设置选中条目的圆角大小 |
| setCurvedEnabled  | 设置是否启用弯曲效果 |
| setCurvedIndicatorSpace  | 设置弯曲效果情况下条目指示器双横线的间距 |
| setCurvedMaxAngle  | 设置弯曲效果情况下最大弯曲角度 |
| setCyclicEnabled  | 设置是否启用无限循环滚动 |
| setData  | 设置数据 |
| setDefaultPosition  | 设置默认选中的位置 |
| setDefaultValue  | 设置默认值 |
| setFormatter  | 设置条目的格式化显示 |
| setIndicatorEnabled  | 设置是否启用条目指示器双横线 |
| setIndicatorColor  | 设置条目指示器双横线的颜色 |
| setIndicatorSize  | 设置条目指示器双横线的大小 |
| setItemSpace  | 设置条目的间距 |
| setMaxWidthText  | 指定条目中最长的文本，便于快速计算条目宽度 |
| setOnWheelChangedListener  | 设置条目滚动监听器 |
| setSameWidthEnabled  | 指定条目中所有文本长度一致，便于快速计算条目宽度 |
| setSelectedTextBold  | 设置条目选中项文本加粗 |
| setSelectedTextColor  | 设置条目选中项文本颜色 |
| setSelectedTextSize  | 设置条目选中项文本字号 |
| setTextAlign  | 设置条目文本对齐方式（`ItemTextAlign`） |
| setTextColor  | 设置条目未选中时的文本颜色 |
| setTextSize  | 设置条目未选中时的文本字号 |
| setTypeface  | 设置条目文本字体（`Typeface`） |
| setVisibleItemCount  | 设置条目可见条数，只能是奇数 |

### XXXPicker

|  方法   | 说明  |
|  ----  | ----  |
| setDefaultValue  | 设置默认值 |
| setDefaultPosition  | 设置默认选中位置 |
| setOnXXXPickedListener  | 设置选择回调监听器 |
| getWheelLayout  | 获取滚轮布局 |
| getWheelView  | 获取滚轮视图 |
| getLabelView  | 获取文本标签视图 |

