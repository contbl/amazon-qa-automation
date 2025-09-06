# language: pt
@api
Funcionalidade: Usuários (API - reqres.in)

  Cenário: Consultar usuário existente
    Dado que a API base é "https://reqres.in"
    Quando eu faço GET em "/api/users/2"
    Então o status deve ser 200
    E o campo "data.first_name" deve ser "Janet"

  Cenário: Criar usuário
    Dado que a API base é "https://reqres.in"
    Quando eu faço POST em "/api/users" com o corpo:
      """
      {
        "name": "rafael",
        "job": "qa"
      }
      """
    Então o status deve ser 201
    E o campo "name" deve ser "rafael"
    E o campo "id" deve existir