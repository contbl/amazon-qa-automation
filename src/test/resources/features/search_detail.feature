# language: pt
@ui
Funcionalidade: Pesquisa e detalhe de produto

  Cenário: Pesquisar por Playstation 5 e abrir a página de detalhes
    Dado que estou na home da Amazon
    Quando eu digito "B0F7Z9F9SD" na busca para buscar
    E eu seleciono o primeiro resultado
    Então a página de detalhes deve ser exibida
