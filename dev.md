#### 公众号取消关注接收到的参数示例
```xml
<xml>
    <ToUserName><![CDATA[gh_59175514e878]]></ToUserName>
    <FromUserName><![CDATA[o1VOowIDMOYLRmk3RznA9vmls2NM]]></FromUserName>
    <CreateTime>1588472527</CreateTime>
    <MsgType><![CDATA[event]]></MsgType>
    <Event><![CDATA[unsubscribe]]></Event>
    <EventKey><![CDATA[]]></EventKey>
    <Encrypt><![CDATA[PbBps9zWKHfuLmXRnEtgPbWr/EmAiCvbNyZuy6DcjdOU8mwyvyR1lrOCLgiYdUJOpDfzXlHktJbxP/Zl82W/RoL6IAjOLBikAWPjjejmvEtYGcOgI97YLNBPW5NeY3o0k6wucW8BwBbAoNE8ZCy6A2i5ftRSOzdkWmJh2M4zz7HXI+YYWGYszZnu3B24bmM2WMvo2naJPsK3trQbtrShGSaeT8gzV0P9pBs4IwkkCs9/Wl3ZTphBwe5LCCl66K2+NTzfPzPiTRY/IPI0S9J1FoSFQneB+OfwTlZTHDFTdHiMGeTETgXAm40V8stNyjimQKFXW6EXZYdxxA/NgyYtPH7X9wmkqO4xmaioNklM1sSKiCmElblSg+FAXg8avsa1F3k5W76GDzK8nkB/47FDmD2ykDdFaIfLeAt2T+ucCm0=]]></Encrypt>
</xml>
```

#### 公众号关注接收到的参数示例
```xml
<xml>
    <ToUserName><![CDATA[gh_59175514e878]]></ToUserName>
    <FromUserName><![CDATA[o1VOowIDMOYLRmk3RznA9vmls2NM]]></FromUserName>
    <CreateTime>1588473584</CreateTime>
    <MsgType><![CDATA[event]]></MsgType>
    <Event><![CDATA[subscribe]]></Event>
    <EventKey><![CDATA[]]></EventKey>
    <Encrypt><![CDATA[O/WAW/oryD8ABdSMhxotsV/PCUzBHOcEtO1ZiuTNtF8qgrI1I4mX+tsO7W6l8AH2G/A4PQHuTEHufsQRE65PY/RkwJSvsWAjMb7eZx1QswDyIR0U8JcwkEuH3a9njAYA6MRrvtHuGKnhilP6TjQyzxWCyWa5EuaO4KSphZByUnExY2Yo+XZ1MGa6qlrPNDbVWZwJnctqwiICMDYv93M/MwgGX5zH10rlMnr0V3U1uLdElPJum2UZN1TjovC9shG0ZDjO72Os5RjWIGiWZ8QlrKhFt1JlkztnWXqiGv6JEMTvMHXrFrm0BjFXdsCoNSv7+cSkRvbBjohJKtTRgeoQRaEB8sAZzycBjUL29ZEn7D8UbYejZkHpdgK2ujfXnPXSLbFMpj5DAo4HAy3rqL8SgUxydSFXjxhxtOGgYr59BSg=]]></Encrypt>
</xml>
```

#### 服务端正式环境地址
`https://bj-haoruxue.cn/easy4enrolwas/wechatclientservice/receiveReply`

#### 发送小卡片（已试验不可用）
```
<mp-miniprogram data-miniprogram-appid="wx123123123" 
data-miniprogram-path="pages/index/index" 
data-miniprogram-title="小程序示例" 
data-progarm-imageurl="http://mmbizqbic.cn/demo.jpg"></mp-miniprogram>
```

#### 雷云冉测试api
* 获取客服列表：`http://127.0.0.1:8080/easy4enrolwas/demo/t14`
* 获取公众号自动回复规则：`http://127.0.0.1:8080/easy4enrolwas/demo/t15`

#### 公众号已有的模板
```
{
    "template_list": [
        {
            "template_id": "yh3G9LN8XJw3_NqIFtsz2rQARY4aOfUfxNN7MvCpUUw",
            "title": "订阅模板消息",
            "primary_industry": "",
            "deputy_industry": "",
            "content": "{{content.DATA}}",
            "example": ""
        },
        {
            "template_id": "YT3EQ3Tm7EFV75q02fAkPEIjM4b-mLGBIsthEkcDTX8",
            "title": "课时变更提醒",
            "primary_industry": "教育",
            "deputy_industry": "培训",
            "content": "{{first.DATA}}\n课程项目：{{keyword1.DATA}}\n课时变化：{{keyword2.DATA}}\n上课日期：{{keyword3.DATA}}\n上课时间：{{keyword4.DATA}}\n{{remark.DATA}}",
            "example": "学员张小明今天上课记录变化啦\r\n课程项目：书法中级班\r\n课时变化：减 2个课时\r\n上课日期：2015年2月1日\r\n上课时间：17:00 - 19:00\r\n感谢您的支持，如有疑问，请随时同我们联系！"
        },
        {
            "template_id": "QmjAoUGgqWW2Au8LNxmAYFCJXBjcHsfZmAZgNVNsRPo",
            "title": "业务办理通知",
            "primary_industry": "教育",
            "deputy_industry": "培训",
            "content": "{{first.DATA}}\n业务名称：{{keyword1.DATA}}\n业务内容：{{keyword2.DATA}}\n实交金额：{{keyword3.DATA}}\n支付方式：{{keyword4.DATA}}\n{{remark.DATA}}",
            "example": "张小明家长，您好，您有一个业务办理通知，请查看\r\n业务名称：新生报名\r\n业务内容：您在我校报名了英语三年级A班，购买20个课时，购买了新概念1册教材一本\r\n实交金额：500元\r\n支付方式：现金支付300元，刷卡支付200元\r\n感谢您的使用"
        }
    ]
}
```

#### 客服列表
```
{
    "kf_list": [
        {
            "kf_account": "kf2001@haoruxuel",
            "kf_headimgurl": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvJVG9L7wfHZoqFMogLDS1zLGmy8ThfzCMBepZeTmY1mQZ0vY4Q8PzcuDRUnpP5dLtw9gbETdzZ9EA/300?wx_fmt=png",
            "kf_id": 2001,
            "kf_nick": "好入学客服 1",
            "kf_wx": "yjf13552109914"
        }
    ]
}
```

#### 在公众号的openId
`o1VOowDXUeQj3RGRHncd_zbdz5IA`

#### 微信公众号返回的信息，会中文乱码，需要转换编码
```
String result = new String(s.getBytes("ISO-8859-1"), "UTF-8");
```
#### 部分素材列表
```
{
    "item": [
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgUW5ZtzVXL17er2xxfqSFDQ",
            "name": "小程序卡片.jpg",
            "update_time": 1588513075,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvJVG9L7wfHZoqFMogLDS1zLzegUtuBYrz7EWJmROhibcbl38pu7H2o9bGd7TibUcsacLy7DJLHEWjww/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgQoXoFlIjY5BGehrJKYlOLM",
            "name": "丰台划片2.png",
            "update_time": 1588309693,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKlWnAt7tDxD4SbY7NVouXpW4FicZsSlzvEKddps053UXCg8d5fBfAQZRgLmUfQdibsHic2BOq5N6Dbg/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgZVYsgMr57O7_2zzROdLFRo",
            "name": "丰台划片1.png",
            "update_time": 1588309692,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKlWnAt7tDxD4SbY7NVouXpYrcX4NSrKbR8wVicRiaFbnf1NgBP5LnxqLQmnHwtXZTUfTNW8A7OrQKg/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgcxF3-zYmT0cn27XZalWHbE",
            "name": "丰台划片2.png",
            "update_time": 1588309565,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKlWnAt7tDxD4SbY7NVouXpMC7od4SqfEibpsc3omSw2ClBwoTeSseq7fHRlsRYtapvIqS846FfR9A/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgZxPRAmaIRzcNw4Z52N1WR0",
            "name": "丰台划片1.png",
            "update_time": 1588309564,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKlWnAt7tDxD4SbY7NVouXpVGJPMMKhiaribxkmia6hGHiaZO57oQ4GztJCMiacb1fVfZNibTMtP697nqhw/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgfqw0UfpXJEhN2y7QOB7-7g",
            "name": "f4c3c18ebc46ad4e2f900052fb2ab840.png",
            "update_time": 1587295014,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF3hLtepviadEp23CrQq6KQibYte6H9d5Bo8pic6RibpAKzhWuZicumFVgzS6Q/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgZZINBf7dfr55-wYXI0YflA",
            "name": "是假的.jpg",
            "update_time": 1587294289,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF3v0BGtb3iaEpKUEjmEWPaZdF5lyalSibOZaSriaMutodIqKRYfdIu0diaXw/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgQyWJHwmE3_DZFa0wM_d1_M",
            "name": "b1aeaec986b34353a5d08f5ba7dbd5ea.jpg",
            "update_time": 1587290830,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF3ibIwdEyYTJLNciaKJULibSX0KLN2a1NwMBro156KLuIw8RQjicSoHL8Xug/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgZ4oZYoPa15wo6O9Pl4B7xE",
            "name": "幼升小.jpg",
            "update_time": 1587290640,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF3dunNSDjR2I7VHqe5TibmTk8c0ia7MYHvibvvp42r0eSyehRshAchGoWMg/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgdMERNKPSnzpbfza3YN5hNc",
            "name": "微信图片_20200419151958.jpg",
            "update_time": 1587289425,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF3BgPicZxNUY3nt7B0vwHIpBr9D5SPXlFBhM55weBjF36I3I78VB8yahw/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgQr_h3Fq0aDwJymsc4xcF_Q",
            "name": "微信图片_20200419155850.jpg",
            "update_time": 1587289426,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF39fDuvCJiaxAdb2qy1s9BmTrI2ibgEAo5UnbRT7zu9ryQpvESiar1JyMEg/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgZHv10HW1RRI4ZUZ-MmT8L8",
            "name": "微信图片_20200419145841.jpg",
            "update_time": 1587289425,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF3hbib2ibG0VFvEJAFfMmHYpAFGejYDcsqdICEWKylXtxibp4D0PFJTTpvw/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgawPfypR0GkgtclLtlO0soY",
            "name": "微信图片_20200419141052.jpg",
            "update_time": 1587289425,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvLq4DCkqnqz9KFPKMlK1XF30dqljwheJasdWm1vPrx89d8wibdsIZwjcdLBTgD6V9m6ouhYDbiaHYMg/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgRh5x3IRp7XzMYhRWywL8c8",
            "name": "入学途径.png",
            "update_time": 1586673854,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwTP1KL763U9ERgoCibwTDxs9ZUvCZkgUNyzeQF7OIyDJ7ya4rvL6eCPg/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgXugWR07svxy_KJZb6IqbVc",
            "name": "房产政策.png",
            "update_time": 1586672918,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwBTd1iacIDV8C7wvPUsQdXdaibayueALHQLY2kAukUUIKmc3FozuWOJwA/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgajNK9-d04LwS1ptb8g3UM4",
            "name": "跨区择校.png",
            "update_time": 1586671905,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwnib00cmibNsGDd2VreFMWhIDoaRSbcTAw0cWKJF0sC0Jt65ho1XOzcpw/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgbZxPIK6kI-HfIHY07Xrrl4",
            "name": "入学顺位.png",
            "update_time": 1586669473,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwQmju4NowuDvELI657awibVpWQeoHVPI1WwFcrlkInHxwe6Pb1n51gfw/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgRvb8Y9nuxvoTPSaMyt0Cj8",
            "name": "户口.jpg",
            "update_time": 1586668354,
            "url": "http://mmbiz.qpic.cn/mmbiz_jpg/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwgiap2Y5GXTbfFClvYFA72wJGgny8xNDaOjPm4TJYd3KDSZJW4aRpNDg/0?wx_fmt=jpeg",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgZGTPSrTLQFV4msp0MkzLms",
            "name": "升学资讯.png",
            "update_time": 1586667616,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwwnKH2sjibopnbgt3ictzr62SgwbmAmXJcA0bHj6XccF6hspu8sCnqU5Q/0?wx_fmt=png",
            "tags": []
        },
        {
            "media_id": "Av8dtigBTqnLJRRSVfRIgVvpi733CLC659yy0KZhPIk",
            "name": "1.png",
            "update_time": 1586666925,
            "url": "http://mmbiz.qpic.cn/mmbiz_png/icrs5iciaNlsvKeQ0AJVfHicTCCoO1BsTCOwT9jlrz9Uglbattib2IVmWnnEFBZr93LADjKvw2BEMhHAt1hJr32UDCg/0?wx_fmt=png",
            "tags": []
        }
    ],
    "total_count": 127,
    "item_count": 20
}
```

#### 待开发
1. 小程序登录，保存到统一账户信息；
2. 关注公众号，保存到统一账户信息；查询获取 unionid
3. ~~每天定时任务，获取公众号 unionid~~
4. 取消关注时，删除统一账户信息里的公众号信息




