select sum(trade.trade_price*trade_volume)/sum(trade_volume) as average_price from tb_trade