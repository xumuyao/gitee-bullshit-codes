
/**
 * 同事说我写了注释
 * @author mjm
 */

		List<OperationPurchaseInfo> purchaseInfoList = sheet.getPurchaseInfoList().stream().filter(purchaseInfo ->
				purchaseInfo.getExteriorOperation().getExteriorPart().getExteriorOperationList().stream()
						.filter(exteriorOperation -> exteriorOperation.getProcessState().equals(ExteriorOperation.ProcessState.PROCESSING)).count() != 0
						//订单明细中工序对应的工件下的其他工序存在加工中，且已发给供应商且供应商不是当前订单供应商时，需要判断
						&& (purchaseInfo.getExteriorOperation().getExteriorPart().getTeamwork() == null ||
						!purchaseInfo.getExteriorOperation().getExteriorPart().getTeamwork().equals(sheet.getTeamwork()))
		).collect(Collectors.toList());