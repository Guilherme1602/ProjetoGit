#Projeto final de Rest API com Spring. Análise do problema.

Controller
cliente - Método: GET, POST, (PUT, DELETE) - só poderá fazer após o login.
pedido - Método: GET(todos os pedidos), POST, PUT - Finalizar pedido, é atualizar um pedido, colocando como status pedido finalizado.

Ao finalizar o pedido, enviar o e-mail para o cliente.

categoria - GET(todos), GET(nome), POST
produto - GET(todos), GET(nome), POST


Ao criar um produto, o usuário tem que ser administrador do ecommerce.

Um pedido é uma composição de um cliente, Lista de produtos, quantidade, valor final, data de envio e data de entrega.

Cliente - CPF, Email, e data nascimento +18 e -120.

Produto - Valor do Produto e quantidade em estoque com valores negativo.(Regra não permitir que o estoque fique vazio).

Pedido - Validação das datas (previsão de entrega).

https://github.com/liagiu/projeto-bd-serratec/blob/main/scripts/script-ddl.sql

