-- Initial data generated from 算力租赁平台_正式开发版_v1.1.md
SET NAMES utf8mb4;

-- Dev only default admin: username=admin, password=admin123. Change password_hash before production use.
INSERT INTO `sys_admin` (`username`, `password_hash`, `nickname`, `role`, `status`) VALUES
('admin', '$2a$10$sETkfpRdhwa2bsRlCl7tEe.HniV6X5y4Rl8dnG8Dae0Ht3u6guK7C', 'Default Admin', 'SUPER_ADMIN', 1)
ON DUPLICATE KEY UPDATE `username` = `username`;
INSERT INTO `rental_cycle_rule` (`cycle_code`, `cycle_name`, `cycle_days`, `yield_multiplier`, `early_penalty_rate`, `sort_no`, `status`) VALUES
('D7',   '7天',   7,   1.0000, 0.0100, 1, 1),
('D15',  '15天',  15,  1.1000, 0.0100, 2, 1),
('D30',  '1个月', 30,  1.2000, 0.0100, 3, 1),
('D90',  '3个月', 90,  1.3000, 0.0100, 4, 1),
('D180', '6个月', 180, 1.4000, 0.0100, 5, 1),
('D360', '12个月',360, 1.5000, 0.0100, 6, 1);

INSERT INTO `commission_rule` (`level_no`, `commission_rate`, `status`) VALUES
(1, 0.2000, 1),
(2, 0.1000, 1),
(3, 0.0500, 1);

INSERT INTO `sys_config` (`config_key`, `config_value`, `config_desc`) VALUES
('withdraw.min_amount',              '10',     '最低提现金额 USDT'),
('withdraw.fee_free_threshold',      '100',    '免手续费门槛 USDT，大于等于此金额免费'),
('withdraw.fee_rate',                '0.05',   '提现手续费比例，小于门槛时收取（5%）'),
('withdraw.max_daily_amount',        '100000', '每日累计提现上限 USDT'),
('recharge.min_amount',              '500',    '全局最低充值金额 USDT'),
('order.activation_timeout_minutes', '60',     '待激活订单超时自动取消时间（分钟），超时退还机器费'),
('order.pending_activation_timeout_minutes', '60', '待激活订单超时自动取消时间（分钟），兼容当前阶段配置键'),
('email_code.rate_limit_per_minute', '5',      '邮箱验证码每分钟最大发送次数');
