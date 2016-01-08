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
	public final static  String APP_ID = "3003546314";

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
	public final static String APPV_KEY = "MIICXQIBAAKBgQDvVyLxe8i4MyOTnP/ZcPYEw+sYhjyqMWiQtA/o5zBNbtj9iOr6mczBZHO2c8BLeLuZTqKzzumIufnBYMA5RcO5wvfoEreCYEmB5LxdZBIetugyf1ZLy21C98pzEvuOvItHU5YToLo5yCjz6cS1Rs6N8f+y+BVx7BshUfW34MdN2wIDAQABAoGAGyYdNps993cFkBb9BqNDpOK8ZjnD4+Ja5B8ottiSqBXs00WWsfqEOWO1pHqY03bMgOVkm1TbNMEzzXW2GEkLnGyQONZxZjAY3lqpzKRudJbpfNgIQDWlJ8e1r0hfzGSP9UZOQpxrb1YgUshe4dysexW7x318DHeetgO6G+OxAOECQQD8nQ3l1VbvE+fHsoctV8llwO88PU6xRXi2Q5OP9fkcko9Ai/Pa87l8UtGu58EZF5Jizhn6VFxXaSwgYZqtUVwTAkEA8oyHtjUjvu4qWV2cJWcrMExkwDOiVNtccWluJS4OI8grW7dgWg6M6kFn+WBsg7mbrl0Doa/vx/b7GgUUHd9wGQJBAIeCaHYmH2BYuTky17jHIs5m1O7npFMMBVkgnwPW2y8sn5Psf3+wxouekcTVpYwkTO6byOu0XWS2kmk8cpEO1+kCQQCsIn3iGz5+L2qQt7WsedaV1XGfUkW/sVqfBnLsIg5Y6KOU2MShxTfOvJJ80ijRJ3XMPijopAMpSCgW5EnNlZhpAkAIxznrAMsSVxC9s1qRw+/IUwwYvJblYJu7uycHyXoEjVnZAJfPCVohTFXFXtQJ0nO0xcCTZ4vmNMgfcsB8ZPWz";

	/**
	 * 平台公钥：
	 * 用于商户应用对接收平台的数据进行解密
	
	 */
	public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC1mB5fRJCdctnWEI1EUEEJdCc6qmXRlQwbZTbSC11S6LpuVXCjYNBb04L9tWWJZQxvaNit3lZk6OmtEUWP5hKoiq9UbT4m9YwNYKw5WZYi2R3J08ODAcYQJW5KoiZCM33zp93Yajy7s//8gW0mQasDDDHeg+BIUCcv261nhMDsQIDAQAB";

}