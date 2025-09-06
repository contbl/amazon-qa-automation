# language: pt
@ui
Funcionalidade: Autocomplete de busca

  Cenário: Exibir sugestões ao digitar termo válido
    Dado que estou na home da Amazon
    Quando eu digito "iphone" na busca
    Então devo ver sugestões de autocomplete
