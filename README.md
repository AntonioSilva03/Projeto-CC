<h1 align="center">Projeto da UC de Comunicações de Computador - 2023/2024</h1>
<h2 align="center">Serviço de transferência de ficheiros Peer-to-Peer</h2>

## Definição
Um serviço peer-to-peer de transferência de ficheiros permite aos clientes transferir dados de forma mais eficiente de vários nós (seeders) transferindo partes diferentes de pacotes em paralelo.

## Elementos deste repositório
Este repositório contém:
- Código para um servidor que vai guardar e coordenar tudo o que acontece na rede
- Aplicação que funciona como cliente, esta vai ser a aplicação que os utilizadores vão utilizar
- Topologia para simular uma rede onde o servidor e os clientes são testados
- Vários ficheiros aleatórios sem significado para simular as transferências (podem ser adicionados outros sem problemas)

## Utilização
- A topologia possui um servidor, um router e alguns nós que funcionam como clientes.
- A aplicação do servidor deve ser executada no servidor na topologia e a aplicação do cliente nos nós. Este servidor consegue lidar com conexões de vários clientes ao mesmo tempo.
- Os clientes tem uma pasta que vai servir como parta partilhada. Todos os ficheiros dessa pasta são públicos na rede.
- As instruções mais detalhadas de como executar o código encontram-se [aqui](https://github.com/Pedrosilva03/cc-peer-to-peer-service/blob/2df2a677a2162bb2b2a3660cfb494af150dceed2/COMO%20USAR.txt).
- Para fazer o download de um ficheiro basta selecionar a opção de download e escrever o nome do ficheiro que pretende transferir. Exemplo: ```ficheiro.txt```

## Funcionalidades
- O cliente pode fazer download de ficheiros de outros clientes em paralelo (dependendo da quantidade de clientes que possuem o ficheiro pedido).
- Da mesma maneira outros clientes podem fazer download de blocos de ficheiros que estão na pasta partilhada. Esta pasta partilhada é enviada para o programa na execução como argumento.
- É possível também consultar os ficheiros que se encontram a ser partilhados naquele momento diretamente no programa, bem como eliminar ficheiros.

## Conclusão
Trabalho realizado por Pedro Silva, António Silva e Diogo Barros.
