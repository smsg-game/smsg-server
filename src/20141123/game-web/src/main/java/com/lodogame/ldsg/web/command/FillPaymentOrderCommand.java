package com.lodogame.ldsg.web.command;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.PaymentOrderDao;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.partner.service.impl.EasouServiceImpl;
import com.lodogame.ldsg.sdk.GameApiSdk;
import com.lodogame.model.PaymentOrder;

/**
 * 计算排名信息
 * 
 * @author chenjian
 * 
 */
public class FillPaymentOrderCommand extends BaseCommand {

	public static Logger LOG = Logger.getLogger(EasouServiceImpl.class);

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	void work() {
		fillPaymentOrder();
	}

	public void fillPaymentOrder() {

		List<PaymentOrder> paymentOrderList = this.paymentOrderDao.getPaymentList(OrderStatus.STATUS_NOT_PAY);

		for (PaymentOrder paymentOrder : paymentOrderList) {
			if (paymentOrder.getStatus() != OrderStatus.STATUS_NOT_PAY) {
				continue;
			}

			if (!paymentOrder.getServerId().equalsIgnoreCase("h10") && !paymentOrder.getServerId().equalsIgnoreCase("h1")) {
				continue;
			}

			int gold = (int) (paymentOrder.getAmount().doubleValue() * 10);

			boolean success = GameApiSdk.getInstance().doPayment(paymentOrder.getPartnerId(), paymentOrder.getServerId(), paymentOrder.getPartnerUserId(), "", paymentOrder.getOrderId(), paymentOrder.getAmount(), gold,
					"", "");

			LOG.info("order:[" + paymentOrder.getOrderId() + "], status[" + paymentOrder.getPartnerOrderId() + "], success[" + success + "]");

			if (success) {
				paymentOrderDao.updateStatus(paymentOrder.getOrderId(), OrderStatus.STATUS_FINISH, paymentOrder.getPartnerOrderId(), paymentOrder.getAmount(), "");
			}

		}

	}

	public static void main(String[] args) {

		BaseCommand command = BaseCommand.getBean(FillPaymentOrderCommand.class);
		try {
			command.run();
		} finally {
			if (command != null) {
				command.exit();
			}
		}
	}
}
