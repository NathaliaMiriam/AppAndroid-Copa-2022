package me.dio.copa.catar.remote.mapper

import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.StadiumDomain
import me.dio.copa.catar.domain.model.Team
import me.dio.copa.catar.remote.model.MatchRemote
import me.dio.copa.catar.remote.model.StadiumRemote
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

// com o código recebido pela API é possível extrair a bandeira e o nome do país, ficando com a estrutura necessária p usar na tela
// isso é possível graças ao Mapper

internal fun List<MatchRemote>.toDomain() = map { it.toDomain() }

fun MatchRemote.toDomain(): MatchDomain {
    return MatchDomain(
        id = "$team1-$team2",
        name = name,
        team1 = team1.toTeam(),
        team2 = team2.toTeam(),
        stadium = stadium.toDomain(),
        date = date.toLocalDateTime(),
    )
}

private fun Date.toLocalDateTime(): LocalDateTime {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

// fun p pegar o nome do país
private fun String.toTeam(): Team {
    return Team(
        flag = getTeamFlag(this),
        displayName = Locale("", this).isO3Country // 'isO3Country' pega as 3 primeiras letras do país
    )
}

// fun p pegar a flag(bandeira), utiliza o código + 127397 (é uma constante que torna possível a obtenção da bandeira do país através do código)
private fun getTeamFlag(team: String): String {
    return team.map {
        String(Character.toChars(it.code + 127397))
    }.joinToString("")
}

fun StadiumRemote.toDomain(): StadiumDomain {
    return StadiumDomain(
        name = name,
        image = image
    )
}
