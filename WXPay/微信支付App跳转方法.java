else if ("appweixin".equals(payType)) {    //WXPay
            try {
                WXPay wxPay = new WXPay(MobileWXAPPPayConfig.getInstance());//配置信息(单例)
                //随机字符串，签名，签名类型会自动添加
                //构建请求参数
                Map<String, String> data = new HashMap<>();
                data.put("body", "sldMall-充值测试");
                //商城订单号
                data.put("out_trade_no", ordersn);
                //设备号,自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
                data.put("device_info", "WEB");
                //标价币种,默认人民币：CNY
                data.put("fee_type", "CNY");
                //订单总金额，单位为分，不能有小数点，去掉
                String total_fee;
                if(SettingManager.getSetting("alipay_mobile_test_enable").equals("1")){  // 1-开启测试, 0-关闭测试
                    total_fee = "1";
                }else{
                    BigDecimal payamount = new BigDecimal(amount).multiply(new BigDecimal(100));
                    total_fee = payamount.toString().split("\\.")[0];
                }
                data.put("total_fee", total_fee);
                //终端IP
//                data.put("spbill_create_ip", request.getHeader("X-Real-IP"));
                data.put("spbill_create_ip", WebUtil.getRealIp(request));
                //通知地址
                String notifyURL = DomainUrlUtil.getSLD_URL_RESOURCES()+ "/wx/upgrade_notify.html";
                data.put("notify_url", notifyURL);
                // 此处指定为扫码支付
                data.put("trade_type", "APP");
                //trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
                data.put("product_id", ordersn);
                //建立时间格式化对象：
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                //获取当前时间，作为订单开始时间获取到的时间类型是long类型的，单位是毫秒
                long currentTime = System.currentTimeMillis();
                String currentTimeStr = dateFormat.format(new Date(currentTime));
                //在这个基础上加上5分钟：作为订单失效时间
                long currentTimeAddFive = currentTime + 5 * 60 * 1000;
                String currentTimeAddFiveStr = dateFormat.format(new Date(currentTimeAddFive));
                //交易起始时间
                data.put("time_start", currentTimeStr);
                //交易结束时间
                data.put("time_expire", currentTimeAddFiveStr);

                Map<String, String> resp = wxPay.unifiedOrder(data);
                if (null == resp) {
                    throw new BusinessException("系统维护稍后重试","250");
                }
                String returnCode = resp.get("return_code");

                if (!"SUCCESS".equals(returnCode)) {
                    throw  new BusinessException("系统维护稍后重试","250");
                }

                String resultCode = resp.get("result_code");
                if (!"SUCCESS".equals(resultCode)) {
                    throw  new BusinessException("系统维护稍后重试","250");
                }

                // 支付跳转链接
                String prepayId = resp.get("prepay_id");
                if (com.mysql.jdbc.StringUtils.isNullOrEmpty(prepayId)) {
                    throw  new BusinessException("系统维护稍后重试","250");
                }
                payData.put("actionType","redirect");
                payData.put("payData",prepayId);
                return SldResponse.success(payData);

            } catch (Exception e) {
                log.error(e);
                throw new BusinessException("支付失败，请重试","250");
            }

        }