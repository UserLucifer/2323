-- Initial data generated from 算力租赁平台_正式开发版_v1.1.md
SET NAMES utf8mb4;

-- Dev only default admin: user_name=admin, password=admin123. Change password_hash before production use.
INSERT INTO `sys_admin` (`user_name`, `password_hash`, `role`, `status`) VALUES
('admin', '$2a$10$sETkfpRdhwa2bsRlCl7tEe.HniV6X5y4Rl8dnG8Dae0Ht3u6guK7C', 'SUPER_ADMIN', 1)
ON DUPLICATE KEY UPDATE `user_name` = `user_name`;
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

INSERT INTO `ai_model` (
  `model_code`, `model_name`, `vendor_name`, `logo_url`,
  `monthly_token_consumption_trillion`, `token_unit_price`, `deploy_tech_fee`,
  `status`, `sort_no`
) VALUES
('GPT_5_4', 'GPT-5.4', 'OpenAI', NULL, NULL, 0.00000000, 0.00000000, 1, 1),
('GPT_5_4_MINI', 'GPT-5.4 mini', 'OpenAI', NULL, NULL, 0.00000000, 0.00000000, 1, 2),
('GPT_5_3_CODEX', 'GPT-5.3 Codex', 'OpenAI', NULL, NULL, 0.00000000, 0.00000000, 1, 3),
('CLAUDE_OPUS_4_5', 'Claude Opus 4.5', 'Anthropic', NULL, NULL, 0.00000000, 0.00000000, 1, 4),
('CLAUDE_SONNET_4_5', 'Claude Sonnet 4.5', 'Anthropic', NULL, NULL, 0.00000000, 0.00000000, 1, 5),
('CLAUDE_HAIKU_4_5', 'Claude Haiku 4.5', 'Anthropic', NULL, NULL, 0.00000000, 0.00000000, 1, 6),
('GEMINI_3_PRO', 'Gemini 3 Pro', 'Google', NULL, NULL, 0.00000000, 0.00000000, 1, 7),
('GEMINI_2_5_PRO', 'Gemini 2.5 Pro', 'Google', NULL, NULL, 0.00000000, 0.00000000, 1, 8),
('GEMINI_2_5_FLASH', 'Gemini 2.5 Flash', 'Google', NULL, NULL, 0.00000000, 0.00000000, 1, 9),
('DEEPSEEK_V3', 'DeepSeek-V3', 'DeepSeek', NULL, NULL, 0.00000000, 0.00000000, 1, 10),
('DEEPSEEK_R1', 'DeepSeek-R1', 'DeepSeek', NULL, NULL, 0.00000000, 0.00000000, 1, 11),
('QWEN3_MAX', 'Qwen3-Max', 'Alibaba Cloud', NULL, NULL, 0.00000000, 0.00000000, 1, 12),
('QWEN3_MAX_THINKING', 'Qwen3-Max-Thinking', 'Alibaba Cloud', NULL, NULL, 0.00000000, 0.00000000, 1, 13),
('QWEN3_235B_A22B', 'Qwen3-235B-A22B', 'Alibaba Cloud', NULL, NULL, 0.00000000, 0.00000000, 1, 14),
('KIMI_K2', 'Kimi K2', 'Moonshot AI', NULL, NULL, 0.00000000, 0.00000000, 1, 15),
('KIMI_K2_THINKING', 'Kimi K2 Thinking', 'Moonshot AI', NULL, NULL, 0.00000000, 0.00000000, 1, 16),
('GLM_4_5', 'GLM-4.5', 'Zhipu AI', NULL, NULL, 0.00000000, 0.00000000, 1, 17),
('GLM_4_5_AIR', 'GLM-4.5-Air', 'Zhipu AI', NULL, NULL, 0.00000000, 0.00000000, 1, 18),
('ERNIE_4_5', 'ERNIE 4.5', 'Baidu', NULL, NULL, 0.00000000, 0.00000000, 1, 19),
('GROK_4_20', 'Grok 4.20', 'xAI', NULL, NULL, 0.00000000, 0.00000000, 1, 20),
('GROK_4_FAST', 'Grok 4 Fast', 'xAI', NULL, NULL, 0.00000000, 0.00000000, 1, 21),
('LLAMA_4_MAVERICK', 'Llama 4 Maverick', 'Meta', NULL, NULL, 0.00000000, 0.00000000, 1, 22),
('LLAMA_4_SCOUT', 'Llama 4 Scout', 'Meta', NULL, NULL, 0.00000000, 0.00000000, 1, 23),
('MISTRAL_LARGE_3', 'Mistral Large 3', 'Mistral AI', NULL, NULL, 0.00000000, 0.00000000, 1, 24),
('MISTRAL_MEDIUM_3', 'Mistral Medium 3', 'Mistral AI', NULL, NULL, 0.00000000, 0.00000000, 1, 25)
ON DUPLICATE KEY UPDATE
  `model_name` = VALUES(`model_name`),
  `vendor_name` = VALUES(`vendor_name`),
  `logo_url` = VALUES(`logo_url`),
  `monthly_token_consumption_trillion` = VALUES(`monthly_token_consumption_trillion`),
  `token_unit_price` = VALUES(`token_unit_price`),
  `deploy_tech_fee` = VALUES(`deploy_tech_fee`),
  `status` = VALUES(`status`),
  `sort_no` = VALUES(`sort_no`);

INSERT INTO `sys_config` (`config_key`, `config_value`, `config_desc`) VALUES
('withdraw.min_amount',              '10',     '最低提现金额 USDT'),
('withdraw.fee_free_threshold',      '100',    '免手续费门槛 USDT，大于等于此金额免费'),
('withdraw.fee_rate',                '0.05',   '提现手续费比例，小于门槛时收取（5%）'),
('withdraw.max_daily_amount',        '100000', '每日累计提现上限 USDT'),
('recharge.min_amount',              '500',    '全局最低充值金额 USDT'),
('order.activation_timeout_minutes', '60',     '待激活订单超时自动取消时间（分钟），超时退还机器费'),
('order.pending_activation_timeout_minutes', '60', '待激活订单超时自动取消时间（分钟），兼容当前阶段配置键'),
('email_code.rate_limit_per_minute', '5',      '邮箱验证码每分钟最大发送次数');
