# 本库模块专用的混淆规则
# 若通过FastJSON、GSON等对省市区县的数据实体进行序列化及反序列化，则不能混淆Province、City等实体类。
#-keep class com.github.gzuliyujiang.wheelpicker.entity.AddressEntity { *;}
#-keep class * extends com.github.gzuliyujiang.wheelpicker.entity.AddressEntity { *;}
