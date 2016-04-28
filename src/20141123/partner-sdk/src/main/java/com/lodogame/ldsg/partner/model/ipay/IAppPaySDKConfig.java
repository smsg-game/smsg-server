package com.lodogame.ldsg.partner.model.ipay;

/**
 *应用接入iAppPay云支付平台sdk集成信息 
 */
public class IAppPaySDKConfig{

	/**
	 * 应用名称：
	 * 应用在iAppPay云支付平台注册的名称
	 */
	public final static  String APP_NAME = "神魔三国";

	/**
	 * 应用编号：
	 * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成 
	 */
	public final static  String APP_ID = "3004968355";

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：元宝1
	 */
	public final static  int WARES_ID_1=3;

	/**
	 * 应用私钥：
	 * 用于对商户应用发送到平台的数据进行加密
	 */
	public final static String APPV_KEY = "MIICWwIBAAKBgQCBduYENd8NprLvQNnh/U8YJcG2WjOB+YGdrKIXuhfKOAPTpNaM9uaKouryAOZMwB3Y6prKBRjNN8DqVvNvAJkjquxPnHlctpmRahI2Ti/qS87bWdUPhwkY5KpUs26Sn21Snz86bvUyfkq5Rxn0ed0WqnrqlZpOUQEBs2R2QUq/lQIDAQABAoGABHHaj8Zxs/xHJGOrl1ClCIUzU1lhb1Lrmkg1AG+JJY4qw3djTk+jtprFSi2xumzWGX5kdg8avODvJJx7mxIAL+a+xRdLEYdRd7HePWpCpUU5Yw+zoSn/ZkJ3v3RJmh4rZbv2QELzcJLwi9Yvh4axLnKWzMUz7lxMLE9OR4TsTgECQQDHhtDzXvoIRo6ZIOGOAdoH9G9E04ggYOuQblO75QK1aXZ+ysC3e4MrtY1Vrl8WXQ4Axei50U0gcjIQygpXGEPBAkEAphuOz1BzP55xthk8HidKTnk5NpsHyOWWeY16QAHklz1xP6G7zHHGQzWCjvX4WaJ+G5IJXhg+YEinpQoOtTJg1QJAWPW/DAN4r1/mDFgguNQShiFxytoVg3ozDT5jjw7YWlIfRwDXU/d50G8uCNmqA6qRp76VuOOc466Gq/2pBpQHAQJAEegcoBrDIdOZ3JCh23pO12DYGxvhXfXqH9ZxnF7X3rn/8GUmEqcdQXeOSI0ZpcgPojc/DUa9yrFqaS9JYJ06tQJAecjYIZktm2oqi0NhKHmzKYKA7WhEOJszLO8aiq/u+yAfip8FpShLPIYfVvt0gnJ5exiQNoV6ifChu8AVTwIhxQ==";

	/**
	 * 平台公钥：
	 * 用于商户应用对接收平台的数据进行解密
	
	 */
	public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCApMdoweZ/kw9lyQMjmjFGucW8Dz5T9Dlg2JFNHH00zIstRtHaCPoonzZEwNusC9dNE39WP3p5P1OwX+eILXpmi/d5k76FOVnINRsouljslWwKMVYKLumeGd2TB4YI2E2eKIoHvULyrfiUx1xAXkajTAN/JpF7V51us5sv0Fwr7wIDAQAB";

}