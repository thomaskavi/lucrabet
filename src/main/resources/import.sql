-- Inserts para a tabela tb_plataformas
INSERT INTO tb_plataformas (link_plataforma) VALUES ('esportesdasorte.bet.br');
INSERT INTO tb_plataformas (link_plataforma) VALUES ('mmabet.bet.br');
INSERT INTO tb_plataformas (link_plataforma) VALUES ('betnacional.com');
INSERT INTO tb_plataformas (link_plataforma) VALUES ('estrela.bet');


-- Inserts para a tabela tb_contas
INSERT INTO tb_contas (nome_conta) VALUES ('thomaskavib');
INSERT INTO tb_contas (nome_conta) VALUES ('letyibraim');
INSERT INTO tb_contas (nome_conta) VALUES ('conta_teste_01');
INSERT INTO tb_contas (nome_conta) VALUES ('usuario_vip');


-- Inserts para a tabela tb_registros_operacoes
-- IMPORTANTE: Os IDs das plataformas e contas (plataforma_id, conta_id) devem corresponder aos IDs gerados
-- pelos inserts anteriores (se for IDENTITY, o H2 começará de 1).
-- Verifique os IDs gerados no console H2 após rodar a aplicação pela primeira vez se houver dúvidas.
-- Assumindo que os IDs de Plataforma e Conta começam em 1 e 2, 3, 4, etc.

INSERT INTO tb_registros_operacoes (plataforma_id, conta_id, data_operacao, valor_deposito, nome_slot_giros, situacao, valor_lucro, saque_completo_feito) VALUES (1, 1, '2025-07-12', 50.00, 'Fortune Tiger 10x', 'Finalizado', 25.50, TRUE);
INSERT INTO tb_registros_operacoes (plataforma_id, conta_id, data_operacao, valor_deposito, nome_slot_giros, situacao, valor_lucro, saque_completo_feito) VALUES (2, 2, '2025-07-13', 100.00, 'Penalty Shoot-out 5x', 'Aguardando', 15.00, FALSE);
INSERT INTO tb_registros_operacoes (plataforma_id, conta_id, data_operacao, valor_deposito, nome_slot_giros, situacao, valor_lucro, saque_completo_feito) VALUES (3, 1, '2025-07-12', 75.00, 'Aviator 1x', 'Finalizado', -10.00, TRUE);