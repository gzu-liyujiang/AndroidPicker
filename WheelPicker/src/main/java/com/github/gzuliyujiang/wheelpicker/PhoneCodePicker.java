/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.wheelpicker;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.wheelpicker.entity.PhoneCodeEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机号前缀选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/10 16:44
 */
@SuppressWarnings("unused")
public class PhoneCodePicker extends OptionPicker {
    public static String JSON = "[{\"prefix\":\"1\",\"en\":\"USA\",\"cn\":\"美国\"},\n" +
            "{\"prefix\":\"1\",\"en\":\"PuertoRico\",\"cn\":\"波多黎各\"},\n" +
            "{\"prefix\":\"1\",\"en\":\"Canada\",\"cn\":\"加拿大\"},\n" +
            "{\"prefix\":\"7\",\"en\":\"Russia\",\"cn\":\"俄罗斯\"},\n" +
            "{\"prefix\":\"7\",\"en\":\"Kazeakhstan\",\"cn\":\"哈萨克斯坦\"},\n" +
            "{\"prefix\":\"20\",\"en\":\"Egypt\",\"cn\":\"埃及\"},\n" +
            "{\"prefix\":\"27\",\"en\":\"South Africa\",\"cn\":\"南非\"},\n" +
            "{\"prefix\":\"30\",\"en\":\"Greece\",\"cn\":\"希腊\"},\n" +
            "{\"prefix\":\"31\",\"en\":\"Netherlands\",\"cn\":\"荷兰\"},\n" +
            "{\"prefix\":\"32\",\"en\":\"Belgium\",\"cn\":\"比利时\"},\n" +
            "{\"prefix\":\"33\",\"en\":\"France\",\"cn\":\"法国\"},\n" +
            "{\"prefix\":\"34\",\"en\":\"Spain\",\"cn\":\"西班牙\"},\n" +
            "{\"prefix\":\"36\",\"en\":\"Hungary\",\"cn\":\"匈牙利\"},\n" +
            "{\"prefix\":\"40\",\"en\":\"Romania\",\"cn\":\"罗马尼亚\"},\n" +
            "{\"prefix\":\"41\",\"en\":\"Switzerland\",\"cn\":\"瑞士\"},\n" +
            "{\"prefix\":\"43\",\"en\":\"Austria\",\"cn\":\"奥地利\"},\n" +
            "{\"prefix\":\"44\",\"en\":\"United Kingdom\",\"cn\":\"英国\"},\n" +
            "{\"prefix\":\"44\",\"en\":\"Jersey\",\"cn\":\"泽西岛\"},\n" +
            "{\"prefix\":\"44\",\"en\":\"Isle of Man\",\"cn\":\"马恩岛\"},\n" +
            "{\"prefix\":\"44\",\"en\":\"Guernsey\",\"cn\":\"根西\"},\n" +
            "{\"prefix\":\"45\",\"en\":\"Denmark\",\"cn\":\"丹麦\"},\n" +
            "{\"prefix\":\"46\",\"en\":\"Sweden\",\"cn\":\"瑞典\"},\n" +
            "{\"prefix\":\"47\",\"en\":\"Norway\",\"cn\":\"挪威\"},\n" +
            "{\"prefix\":\"48\",\"en\":\"Poland\",\"cn\":\"波兰\"},\n" +
            "{\"prefix\":\"51\",\"en\":\"Peru\",\"cn\":\"秘鲁\"},\n" +
            "{\"prefix\":\"52\",\"en\":\"Mexico\",\"cn\":\"墨西哥\"},\n" +
            "{\"prefix\":\"53\",\"en\":\"Cuba\",\"cn\":\"古巴\"},\n" +
            "{\"prefix\":\"54\",\"en\":\"Argentina\",\"cn\":\"阿根廷\"},\n" +
            "{\"prefix\":\"55\",\"en\":\"Brazill\",\"cn\":\"巴西\"},\n" +
            "{\"prefix\":\"56\",\"en\":\"Chile\",\"cn\":\"智利\"},\n" +
            "{\"prefix\":\"57\",\"en\":\"Colombia\",\"cn\":\"哥伦比亚\"},\n" +
            "{\"prefix\":\"58\",\"en\":\"Venezuela\",\"cn\":\"委内瑞拉\"},\n" +
            "{\"prefix\":\"60\",\"en\":\"Malaysia\",\"cn\":\"马来西亚\"},\n" +
            "{\"prefix\":\"61\",\"en\":\"Australia\",\"cn\":\"澳大利亚\"},\n" +
            "{\"prefix\":\"62\",\"en\":\"Indonesia\",\"cn\":\"印度尼西亚\"},\n" +
            "{\"prefix\":\"63\",\"en\":\"Philippines\",\"cn\":\"菲律宾\"},\n" +
            "{\"prefix\":\"64\",\"en\":\"NewZealand\",\"cn\":\"新西兰\"},\n" +
            "{\"prefix\":\"65\",\"en\":\"Singapore\",\"cn\":\"新加坡\"},\n" +
            "{\"prefix\":\"66\",\"en\":\"Thailand\",\"cn\":\"泰国\"},\n" +
            "{\"prefix\":\"81\",\"en\":\"Japan\",\"cn\":\"日本\"},\n" +
            "{\"prefix\":\"82\",\"en\":\"Korea\",\"cn\":\"韩国\"},\n" +
            "{\"prefix\":\"84\",\"en\":\"Vietnam\",\"cn\":\"越南\"},\n" +
            "{\"prefix\":\"86\",\"en\":\"China\",\"cn\":\"中国\"},\n" +
            "{\"prefix\":\"90\",\"en\":\"Turkey\",\"cn\":\"土耳其\"},\n" +
            "{\"prefix\":\"91\",\"en\":\"Indea\",\"cn\":\"印度\"},\n" +
            "{\"prefix\":\"92\",\"en\":\"Pakistan\",\"cn\":\"巴基斯坦\"},\n" +
            "{\"prefix\":\"93\",\"en\":\"Italy\",\"cn\":\"意大利\"},\n" +
            "{\"prefix\":\"93\",\"en\":\"Afghanistan\",\"cn\":\"阿富汗\"},\n" +
            "{\"prefix\":\"94\",\"en\":\"SriLanka\",\"cn\":\"斯里兰卡\"},\n" +
            "{\"prefix\":\"94\",\"en\":\"Germany\",\"cn\":\"德国\"},\n" +
            "{\"prefix\":\"95\",\"en\":\"Myanmar\",\"cn\":\"缅甸\"},\n" +
            "{\"prefix\":\"98\",\"en\":\"Iran\",\"cn\":\"伊朗\"},\n" +
            "{\"prefix\":\"212\",\"en\":\"Morocco\",\"cn\":\"摩洛哥\"},\n" +
            "{\"prefix\":\"213\",\"en\":\"Algera\",\"cn\":\"阿尔格拉\"},\n" +
            "{\"prefix\":\"216\",\"en\":\"Tunisia\",\"cn\":\"突尼斯\"},\n" +
            "{\"prefix\":\"218\",\"en\":\"Libya\",\"cn\":\"利比亚\"},\n" +
            "{\"prefix\":\"220\",\"en\":\"Gambia\",\"cn\":\"冈比亚\"},\n" +
            "{\"prefix\":\"221\",\"en\":\"Senegal\",\"cn\":\"塞内加尔\"},\n" +
            "{\"prefix\":\"222\",\"en\":\"Mauritania\",\"cn\":\"毛里塔尼亚\"},\n" +
            "{\"prefix\":\"223\",\"en\":\"Mali\",\"cn\":\"马里\"},\n" +
            "{\"prefix\":\"224\",\"en\":\"Guinea\",\"cn\":\"几内亚\"},\n" +
            "{\"prefix\":\"225\",\"en\":\"Cote divoire\",\"cn\":\"科特迪沃\"},\n" +
            "{\"prefix\":\"226\",\"en\":\"Burkina Faso\",\"cn\":\"布基纳法索\"},\n" +
            "{\"prefix\":\"227\",\"en\":\"Niger\",\"cn\":\"尼日尔\"},\n" +
            "{\"prefix\":\"228\",\"en\":\"Togo\",\"cn\":\"多哥\"},\n" +
            "{\"prefix\":\"229\",\"en\":\"Benin\",\"cn\":\"贝宁\"},\n" +
            "{\"prefix\":\"230\",\"en\":\"Mauritius\",\"cn\":\"毛里求斯\"},\n" +
            "{\"prefix\":\"231\",\"en\":\"Liberia\",\"cn\":\"利比里亚\"},\n" +
            "{\"prefix\":\"232\",\"en\":\"Sierra Leone\",\"cn\":\"塞拉利昂\"},\n" +
            "{\"prefix\":\"233\",\"en\":\"Ghana\",\"cn\":\"加纳\"},\n" +
            "{\"prefix\":\"234\",\"en\":\"Nigeria\",\"cn\":\"尼日利亚\"},\n" +
            "{\"prefix\":\"235\",\"en\":\"Chad\",\"cn\":\"乍得\"},\n" +
            "{\"prefix\":\"236\",\"en\":\"Central African Republic\",\"cn\":\"中非共和国\"},\n" +
            "{\"prefix\":\"237\",\"en\":\"Cameroon\",\"cn\":\"喀麦隆\"},\n" +
            "{\"prefix\":\"238\",\"en\":\"Cape Verde\",\"cn\":\"佛得角\"},\n" +
            "{\"prefix\":\"239\",\"en\":\"Sao Tome and Principe\",\"cn\":\"圣多美和普林西比\"},\n" +
            "{\"prefix\":\"240\",\"en\":\"Guinea\",\"cn\":\"几内亚\"},\n" +
            "{\"prefix\":\"241\",\"en\":\"Gabon\",\"cn\":\"加蓬\"},\n" +
            "{\"prefix\":\"242\",\"en\":\"Republic of the Congo\",\"cn\":\"刚果共和国\"},\n" +
            "{\"prefix\":\"243\",\"en\":\"Democratic Republic of the Congo\",\"cn\":\"刚果民主共和国\"},\n" +
            "{\"prefix\":\"244\",\"en\":\"Angola\",\"cn\":\"安哥拉\"},\n" +
            "{\"prefix\":\"247\",\"en\":\"Ascension\",\"cn\":\"阿森松岛\"},\n" +
            "{\"prefix\":\"248\",\"en\":\"Seychelles\",\"cn\":\"塞舌尔\"},\n" +
            "{\"prefix\":\"249\",\"en\":\"Sudan\",\"cn\":\"苏丹\"},\n" +
            "{\"prefix\":\"250\",\"en\":\"Rwanda\",\"cn\":\"卢旺达\"},\n" +
            "{\"prefix\":\"251\",\"en\":\"Ethiopia\",\"cn\":\"埃塞俄比亚\"},\n" +
            "{\"prefix\":\"253\",\"en\":\"Djibouti\",\"cn\":\"吉布提\"},\n" +
            "{\"prefix\":\"254\",\"en\":\"Kenya\",\"cn\":\"肯尼亚\"},\n" +
            "{\"prefix\":\"255\",\"en\":\"Tanzania\",\"cn\":\"坦桑尼亚\"},\n" +
            "{\"prefix\":\"256\",\"en\":\"Uganda\",\"cn\":\"乌干达\"},\n" +
            "{\"prefix\":\"257\",\"en\":\"Burundi\",\"cn\":\"布隆迪\"},\n" +
            "{\"prefix\":\"258\",\"en\":\"Mozambique\",\"cn\":\"莫桑比克\"},\n" +
            "{\"prefix\":\"260\",\"en\":\"Zambia\",\"cn\":\"赞比亚\"},\n" +
            "{\"prefix\":\"261\",\"en\":\"Madagascar\",\"cn\":\"马达加斯加\"},\n" +
            "{\"prefix\":\"262\",\"en\":\"Reunion\",\"cn\":\"留尼汪\"},\n" +
            "{\"prefix\":\"262\",\"en\":\"Mayotte\",\"cn\":\"马约特\"},\n" +
            "{\"prefix\":\"263\",\"en\":\"Zimbabwe\",\"cn\":\"津巴布韦\"},\n" +
            "{\"prefix\":\"264\",\"en\":\"Namibia\",\"cn\":\"纳米比亚\"},\n" +
            "{\"prefix\":\"265\",\"en\":\"Malawi\",\"cn\":\"马拉维\"},\n" +
            "{\"prefix\":\"266\",\"en\":\"Lesotho\",\"cn\":\"莱索托\"},\n" +
            "{\"prefix\":\"267\",\"en\":\"Botwana\",\"cn\":\"博茨瓦纳\"},\n" +
            "{\"prefix\":\"268\",\"en\":\"Swaziland\",\"cn\":\"斯威士兰\"},\n" +
            "{\"prefix\":\"269\",\"en\":\"Comoros\",\"cn\":\"科摩罗\"},\n" +
            "{\"prefix\":\"297\",\"en\":\"Aruba\",\"cn\":\"阿鲁巴\"},\n" +
            "{\"prefix\":\"298\",\"en\":\"Faroe Islands\",\"cn\":\"法罗群岛\"},\n" +
            "{\"prefix\":\"299\",\"en\":\"Greenland\",\"cn\":\"格陵兰\"},\n" +
            "{\"prefix\":\"350\",\"en\":\"Gibraltar\",\"cn\":\"直布罗陀\"},\n" +
            "{\"prefix\":\"351\",\"en\":\"Portugal\",\"cn\":\"葡萄牙\"},\n" +
            "{\"prefix\":\"352\",\"en\":\"Luxembourg\",\"cn\":\"卢森堡\"},\n" +
            "{\"prefix\":\"353\",\"en\":\"Ireland\",\"cn\":\"爱尔兰\"},\n" +
            "{\"prefix\":\"354\",\"en\":\"Iceland\",\"cn\":\"冰岛\"},\n" +
            "{\"prefix\":\"355\",\"en\":\"Albania\",\"cn\":\"阿尔巴尼亚\"},\n" +
            "{\"prefix\":\"356\",\"en\":\"Malta\",\"cn\":\"马耳他\"},\n" +
            "{\"prefix\":\"357\",\"en\":\"Cyprus\",\"cn\":\"塞浦路斯\"},\n" +
            "{\"prefix\":\"358\",\"en\":\"Finland\",\"cn\":\"芬兰\"},\n" +
            "{\"prefix\":\"359\",\"en\":\"Bulgaria\",\"cn\":\"保加利亚\"},\n" +
            "{\"prefix\":\"370\",\"en\":\"Lithuania\",\"cn\":\"立陶宛\"},\n" +
            "{\"prefix\":\"371\",\"en\":\"Latvia\",\"cn\":\"拉脱维亚\"},\n" +
            "{\"prefix\":\"372\",\"en\":\"Estonia\",\"cn\":\"爱沙尼亚\"},\n" +
            "{\"prefix\":\"373\",\"en\":\"Moldova\",\"cn\":\"摩尔多瓦\"},\n" +
            "{\"prefix\":\"374\",\"en\":\"Armenia\",\"cn\":\"亚美尼亚\"},\n" +
            "{\"prefix\":\"375\",\"en\":\"Belarus\",\"cn\":\"白俄罗斯\"},\n" +
            "{\"prefix\":\"376\",\"en\":\"Andorra\",\"cn\":\"安道尔\"},\n" +
            "{\"prefix\":\"377\",\"en\":\"Monaco\",\"cn\":\"摩纳哥\"},\n" +
            "{\"prefix\":\"378\",\"en\":\"San Marino\",\"cn\":\"圣马力诺\"},\n" +
            "{\"prefix\":\"380\",\"en\":\"Ukraine\",\"cn\":\"乌克兰\"},\n" +
            "{\"prefix\":\"381\",\"en\":\"Serbia\",\"cn\":\"塞尔维亚\"},\n" +
            "{\"prefix\":\"382\",\"en\":\"Montenegro\",\"cn\":\"黑山\"},\n" +
            "{\"prefix\":\"383\",\"en\":\"Kosovo\",\"cn\":\"科索沃\"},\n" +
            "{\"prefix\":\"385\",\"en\":\"Croatia\",\"cn\":\"克罗地亚\"},\n" +
            "{\"prefix\":\"386\",\"en\":\"Slovenia\",\"cn\":\"斯洛文尼亚\"},\n" +
            "{\"prefix\":\"387\",\"en\":\"Bosnia and Herzegovina\",\"cn\":\"波斯尼亚和黑塞哥维那\"},\n" +
            "{\"prefix\":\"389\",\"en\":\"Macedonia\",\"cn\":\"马其顿\"},\n" +
            "{\"prefix\":\"420\",\"en\":\"Czech Republic\",\"cn\":\"捷克共和国\"},\n" +
            "{\"prefix\":\"421\",\"en\":\"Slovakia\",\"cn\":\"斯洛伐克\"},\n" +
            "{\"prefix\":\"423\",\"en\":\"Liechtenstein\",\"cn\":\"列支敦士登\"},\n" +
            "{\"prefix\":\"501\",\"en\":\"Belize\",\"cn\":\"伯利兹\"},\n" +
            "{\"prefix\":\"502\",\"en\":\"Guatemala\",\"cn\":\"危地马拉\"},\n" +
            "{\"prefix\":\"503\",\"en\":\"EISalvador\",\"cn\":\"艾萨尔瓦多\"},\n" +
            "{\"prefix\":\"504\",\"en\":\"Honduras\",\"cn\":\"洪都拉斯\"},\n" +
            "{\"prefix\":\"505\",\"en\":\"Nicaragua\",\"cn\":\"尼加拉瓜\"},\n" +
            "{\"prefix\":\"506\",\"en\":\"Costa Rica\",\"cn\":\"哥斯达黎加\"},\n" +
            "{\"prefix\":\"507\",\"en\":\"Panama\",\"cn\":\"巴拿马\"},\n" +
            "{\"prefix\":\"509\",\"en\":\"Haiti\",\"cn\":\"海地\"},\n" +
            "{\"prefix\":\"590\",\"en\":\"Guadeloupe\",\"cn\":\"瓜德罗普\"},\n" +
            "{\"prefix\":\"591\",\"en\":\"Bolivia\",\"cn\":\"玻利维亚\"},\n" +
            "{\"prefix\":\"592\",\"en\":\"Guyana\",\"cn\":\"圭亚那\"},\n" +
            "{\"prefix\":\"593\",\"en\":\"Ecuador\",\"cn\":\"厄瓜多尔\"},\n" +
            "{\"prefix\":\"594\",\"en\":\"French Guiana\",\"cn\":\"法属圭亚那\"},\n" +
            "{\"prefix\":\"595\",\"en\":\"Paraguay\",\"cn\":\"巴拉圭\"},\n" +
            "{\"prefix\":\"596\",\"en\":\"Martinique\",\"cn\":\"马提尼克\"},\n" +
            "{\"prefix\":\"597\",\"en\":\"Suriname\",\"cn\":\"苏里南\"},\n" +
            "{\"prefix\":\"598\",\"en\":\"Uruguay\",\"cn\":\"乌拉圭\"},\n" +
            "{\"prefix\":\"599\",\"en\":\"Netherlands Antillse\",\"cn\":\"荷属安的列斯\"},\n" +
            "{\"prefix\":\"670\",\"en\":\"Timor Leste\",\"cn\":\"东帝汶\"},\n" +
            "{\"prefix\":\"673\",\"en\":\"Brunei\",\"cn\":\"文莱\"},\n" +
            "{\"prefix\":\"675\",\"en\":\"Papua New Guinea\",\"cn\":\"巴布亚新几内亚\"},\n" +
            "{\"prefix\":\"676\",\"en\":\"Tonga\",\"cn\":\"汤加\"},\n" +
            "{\"prefix\":\"678\",\"en\":\"Vanuatu\",\"cn\":\"瓦努阿图\"},\n" +
            "{\"prefix\":\"679\",\"en\":\"Fiji\",\"cn\":\"斐济\"},\n" +
            "{\"prefix\":\"682\",\"en\":\"Cook Islands\",\"cn\":\"库克群岛\"},\n" +
            "{\"prefix\":\"684\",\"en\":\"Samoa Eastern\",\"cn\":\"萨摩亚东部\"},\n" +
            "{\"prefix\":\"685\",\"en\":\"Samoa Western\",\"cn\":\"萨摩亚西部\"},\n" +
            "{\"prefix\":\"687\",\"en\":\"New Caledonia\",\"cn\":\"新喀里多尼亚\"},\n" +
            "{\"prefix\":\"689\",\"en\":\"French Polynesia\",\"cn\":\"法属波利尼西亚\"},\n" +
            "{\"prefix\":\"852\",\"en\":\"Hong Kong\",\"cn\":\"香港\"},\n" +
            "{\"prefix\":\"853\",\"en\":\"Macao\",\"cn\":\"澳门\"},\n" +
            "{\"prefix\":\"855\",\"en\":\"Cambodia\",\"cn\":\"柬埔寨\"},\n" +
            "{\"prefix\":\"856\",\"en\":\"Laos\",\"cn\":\"老挝\"},\n" +
            "{\"prefix\":\"880\",\"en\":\"Bangladesh\",\"cn\":\"孟加拉国\"},\n" +
            "{\"prefix\":\"886\",\"en\":\"Taiwan\",\"cn\":\"台湾\"},\n" +
            "{\"prefix\":\"960\",\"en\":\"Maldives\",\"cn\":\"马尔代夫\"},\n" +
            "{\"prefix\":\"961\",\"en\":\"Lebanon\",\"cn\":\"黎巴嫩\"},\n" +
            "{\"prefix\":\"962\",\"en\":\"Jordan\",\"cn\":\"约旦\"},\n" +
            "{\"prefix\":\"963\",\"en\":\"Syria\",\"cn\":\"叙利亚\"},\n" +
            "{\"prefix\":\"964\",\"en\":\"Iraq\",\"cn\":\"伊拉克\"},\n" +
            "{\"prefix\":\"965\",\"en\":\"Kuwait\",\"cn\":\"科威特\"},\n" +
            "{\"prefix\":\"966\",\"en\":\"Saudi Arabia\",\"cn\":\"沙特阿拉伯\"},\n" +
            "{\"prefix\":\"967\",\"en\":\"Yemen\",\"cn\":\"也门\"},\n" +
            "{\"prefix\":\"968\",\"en\":\"Oman\",\"cn\":\"阿曼\"},\n" +
            "{\"prefix\":\"970\",\"en\":\"Palestinian\",\"cn\":\"巴勒斯坦\"},\n" +
            "{\"prefix\":\"971\",\"en\":\"United Arab Emirates\",\"cn\":\"阿拉伯联合酋长国\"},\n" +
            "{\"prefix\":\"972\",\"en\":\"Israel\",\"cn\":\"以色列\"},\n" +
            "{\"prefix\":\"973\",\"en\":\"Bahrain\",\"cn\":\"巴林\"},\n" +
            "{\"prefix\":\"974\",\"en\":\"Qotar\",\"cn\":\"库塔\"},\n" +
            "{\"prefix\":\"975\",\"en\":\"Bhutan\",\"cn\":\"不丹\"},\n" +
            "{\"prefix\":\"976\",\"en\":\"Mongolia\",\"cn\":\"蒙古\"},\n" +
            "{\"prefix\":\"977\",\"en\":\"Nepal\",\"cn\":\"尼泊尔\"},\n" +
            "{\"prefix\":\"992\",\"en\":\"Tajikistan\",\"cn\":\"塔吉克斯坦\"},\n" +
            "{\"prefix\":\"993\",\"en\":\"Turkmenistan\",\"cn\":\"土库曼斯坦\"},\n" +
            "{\"prefix\":\"994\",\"en\":\"Azerbaijan\",\"cn\":\"阿塞拜疆\"},\n" +
            "{\"prefix\":\"995\",\"en\":\"Georgia\",\"cn\":\"格鲁吉亚\"},\n" +
            "{\"prefix\":\"996\",\"en\":\"Kyrgyzstan\",\"cn\":\"吉尔吉斯斯坦\"},\n" +
            "{\"prefix\":\"998\",\"en\":\"Uzbekistan\",\"cn\":\"乌兹别克斯坦\"},\n" +
            "{\"prefix\":\"1242\",\"en\":\"Bahamas\",\"cn\":\"巴哈马\"},\n" +
            "{\"prefix\":\"1246\",\"en\":\"Barbados\",\"cn\":\"巴巴多斯\"},\n" +
            "{\"prefix\":\"1264\",\"en\":\"Anguilla\",\"cn\":\"安圭拉\"},\n" +
            "{\"prefix\":\"1268\",\"en\":\"Antigua and Barbuda\",\"cn\":\"安提瓜和巴布达\"},\n" +
            "{\"prefix\":\"1340\",\"en\":\"Virgin Islands\",\"cn\":\"维尔京群岛\"},\n" +
            "{\"prefix\":\"1345\",\"en\":\"Cayman Islands\",\"cn\":\"开曼群岛\"},\n" +
            "{\"prefix\":\"1441\",\"en\":\"Bermuda\",\"cn\":\"百慕大\"},\n" +
            "{\"prefix\":\"1473\",\"en\":\"Grenada\",\"cn\":\"格林纳达\"},\n" +
            "{\"prefix\":\"1649\",\"en\":\"Turks and Caicos Islands\",\"cn\":\"特克斯和凯科斯群岛\"},\n" +
            "{\"prefix\":\"1664\",\"en\":\"Montserrat\",\"cn\":\"蒙特塞拉特\"},\n" +
            "{\"prefix\":\"1671\",\"en\":\"Guam\",\"cn\":\"关岛\"},\n" +
            "{\"prefix\":\"1758\",\"en\":\"St.Lucia\",\"cn\":\"圣卢西亚\"},\n" +
            "{\"prefix\":\"1767\",\"en\":\"Dominica\",\"cn\":\"多米尼加\"},\n" +
            "{\"prefix\":\"1784\",\"en\":\"St.Vincent\",\"cn\":\"圣文森特\"},\n" +
            "{\"prefix\":\"1809\",\"en\":\"Dominican Republic\",\"cn\":\"多米尼加共和国\"},\n" +
            "{\"prefix\":\"1868\",\"en\":\"Trinidad and Tobago\",\"cn\":\"特立尼达和多巴哥\"},\n" +
            "{\"prefix\":\"1869\",\"en\":\"St Kitts and Nevis\",\"cn\":\"圣基茨和尼维斯\"},\n" +
            "{\"prefix\":\"1876\",\"en\":\"Jamaica\",\"cn\":\"牙买加\"}]";
    private boolean onlyChina = false;

    public PhoneCodePicker(@NonNull Activity activity) {
        super(activity);
    }

    public PhoneCodePicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    public void setOnlyChina(boolean onlyChina) {
        this.onlyChina = onlyChina;
        setData(provideData());
    }

    @Override
    public void setDefaultValue(Object item) {
        if (item instanceof String) {
            setDefaultValueByName(item.toString());
        } else {
            super.setDefaultValue(item);
        }
    }

    public void setDefaultValueByCode(String code) {
        PhoneCodeEntity entity = new PhoneCodeEntity();
        entity.setCode(code);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueByName(String name) {
        PhoneCodeEntity entity = new PhoneCodeEntity();
        entity.setName(name);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueByEnglish(String english) {
        PhoneCodeEntity entity = new PhoneCodeEntity();
        entity.setEnglish(english);
        super.setDefaultValue(entity);
    }

    @Override
    protected List<?> provideData() {
        List<PhoneCodeEntity> data = new ArrayList<>();
        if (onlyChina) {
            PhoneCodeEntity china = new PhoneCodeEntity();
            china.setCode("+86");
            china.setName("中国大陆+86");
            china.setEnglish("Chinese Mainland");
            data.add(china);
            PhoneCodeEntity hongKong = new PhoneCodeEntity();
            hongKong.setCode("+852");
            hongKong.setName("香港+852");
            hongKong.setEnglish("Hong Kong");
            data.add(hongKong);
            PhoneCodeEntity macao = new PhoneCodeEntity();
            macao.setCode("+853");
            macao.setName("澳门+853");
            macao.setEnglish("Macao");
            data.add(macao);
            PhoneCodeEntity taiwan = new PhoneCodeEntity();
            taiwan.setCode("+886");
            taiwan.setName("台湾+886");
            taiwan.setEnglish("Taiwan");
            data.add(taiwan);
        } else {
            try {
                JSONArray jsonArray = new JSONArray(JSON);
                for (int i = 0, n = jsonArray.length(); i < n; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PhoneCodeEntity entity = new PhoneCodeEntity();
                    entity.setCode("+" + jsonObject.getString("prefix"));
                    entity.setName(jsonObject.getString("cn"));
                    entity.setEnglish(jsonObject.getString("en"));
                    data.add(entity);
                }
            } catch (JSONException e) {
                DialogLog.print(e);
            }
        }
        return data;
    }

}
