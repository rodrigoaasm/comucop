<h1>COMUCOP</h1>
<h2> Description</h2>
The Comucop is a system of communication between the
which aims to create lines of communication between different office employees.
<h2> Motivation</h2>
The system was implemented for command COM242.1 - DISTRIBUTED SYSTEMS of the course of
INFORMATION SYSTEMS of the INSTITUTE OF MATHEMATICS AND COMPUTING
<a href=https://unifei.edu.br/> UNIVERSIDADE FEDERAL DE ITAJUBÁ (UNIFEI) </a> Location of the city of ITAJUBÁ / MG - BRAZIL
<h2> Implementation strategy</h2>
The system relies on a chat where messages are encapsulated in a JSON and transmitted via TCP / IP protocol to a
server that sends the message to the recipient if it is online,
stores a message in the NOSQL MongoDB database until the recipient is available to receive it.
<h2> Technologies</h2>
<ul>
<li> Java language </li>
<li> JSON file </li>
<li> <a href=http://hibernate.org/> Hibernate </a> </ li>
<li> <a href=https://www.mongodb.com/> MongoDB </a> </ li>
<li> <a href=https://www.mysql.com/> MySQL </a> </ li>
</ul>


<h1>COMUCOP (PT-br)</h1>
<h2>Descrição (PT-br)</h2>
O Comucop é um sistema de comunicação corporativa entre setores
que visa cria linhas de comunicação entre funcionários de setores diferentes.
<h2>Motivação (PT-br)</h2>
O sistema foi apresentado para disciplina COM242.1 - SISTEMAS DISTRIBUÍDOS do curso de 
SISTEMAS DE INFORMAÇÃO do INSTITUTO DE MATEMATICA E COMPUTAÇÃO da 
<a href=https://unifei.edu.br/ >UNIVERSIDADE FEDERAL DE ITAJUBÁ (UNIFEI)</a> localizada na cidade de ITAJUBÁ/MG - BRASIL
<h2>Estratégia de implementação (PT-br)</h2>
O sistema se baseia em um chat onde as mensagens são encapsuladas em um JSON e transmitidas via protocolo TCP/IP até um 
servidor que faz o redirecionamento da mensagem ao destinatário caso ele esteja online, se o mesmo não estiver online 
armazena a mensagem no banco NOSQL MongoDB até o destinatário encontre-se disponível a recebê-la.
<h2>Tecnologias (PT-br)</h2>
<ul>
  <li>Linguagem Java</li>
  <li>Arquivo JSON</li>
  <li><a href=http://hibernate.org/ >Hibernate</a></li>
  <li><a href=https://www.mongodb.com/ >MongoDB</a></li>
  <li><a href=https://www.mysql.com/ >MySQL</a></li>
</ul>
