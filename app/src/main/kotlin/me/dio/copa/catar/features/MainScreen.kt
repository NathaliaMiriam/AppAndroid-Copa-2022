package me.dio.copa.catar.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.R
import me.dio.copa.catar.domain.extensions.getDate
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.ui.theme.Shapes

// essa classe desenha, dá corpo para a tela

typealias NotificationOnClick = (match: MatchDomain) -> Unit

// recebe a lista de matches (partidas)
@Composable
fun MainScreen(matches: List<MatchDomain>, onNotificationClick: NotificationOnClick) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { // coloca um espaçamento entre um card e outro
            items(matches) { match ->
                MatchInfo(match, onNotificationClick)
            }
        }
    }
}

@Composable
fun MatchInfo(match: MatchDomain, onNotificationClick: NotificationOnClick) {
    Card(
        // coloca a borda arredondada do card, pegando a tela completa na horizontal
        shape = Shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            // coloca e edita a imagem do card ... a imagem vêm de um json criado pela DIO
            AsyncImage(
                model = match.stadium.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(160.dp)
            )
            // coloca a coluna, com 3 linhas: p notificação, p título com data e horário, p times que irão jogar
            Column(modifier = Modifier.padding(16.dp)) {
                Notification(match, onNotificationClick)
                Title(match)
                Teams(match)
            }
        }
    }
}

// fun que alinha as notificações
@Composable
fun Notification(match: MatchDomain, onClick: NotificationOnClick) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { // pega a linha toda e coloca a not. no fim dela
        val drawable = if (match.notificationEnabled) R.drawable.ic_notifications_active // se a notificação estiver habilitada, pega o ícone de not. habilitada
        else R.drawable.ic_notifications // se não, pega o ícone de not. desabilitada

        Image(
            painter = painterResource(id = drawable),
            modifier = Modifier.clickable {
                onClick(match)
            },
            contentDescription = null
        )
    }
}

// fun que alinha o título
@Composable
fun Title(match: MatchDomain) {
    // linha alinhada horizontalmente
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // texto
        Text(
            text = "${match.date.getDate()} - ${match.name}",
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )
    }
}

// fun que alinha os times que irão jogar
@Composable
fun Teams(match: MatchDomain) {
    // linhas alinhadas horizontalmente
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically, // essa linha foi colocada no centro e na vertical
    ) {
        TeamItem(team = match.team1) // time 1

        // alinha o caracter que fica entre os dois times, que simboliza o 'versos'
        Text(
            text = "X",
            modifier = Modifier.padding(end = 16.dp, start = 16.dp),
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )

        TeamItem(team = match.team2) // time 2
    }
}

// fun que alinha as bandeiras dos países junto aos nomes dos times ...
// classes que buscam e config. as bandeiras e os nomes dos times --> Team e MatchMapper
@Composable
fun TeamItem(team: TeamDomain) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // alinha as bandeiras
        Text(
            text = team.flag, // flag configurada na data class 'Team' em 'model'
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.h3.copy(color = Color.White)
        )

        // coloca um espaço entre as bandeiras e os nomes dos países
        Spacer(modifier = Modifier.size(16.dp))

        // alinha os nomes dos países
        Text(
            text = team.displayName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )
    }
}
