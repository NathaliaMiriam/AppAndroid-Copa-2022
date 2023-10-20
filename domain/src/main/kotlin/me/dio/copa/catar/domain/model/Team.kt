package me.dio.copa.catar.domain.model

typealias TeamDomain = Team

// aqui configura a flag com o conceito de Mappers...
// Ctrl + clicar em flag + selecionar a classe MatchMapper e ela se abri-ra

data class Team(
    val flag: String,
    val displayName: String
)
