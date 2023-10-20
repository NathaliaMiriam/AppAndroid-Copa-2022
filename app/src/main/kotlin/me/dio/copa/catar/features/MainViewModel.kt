package me.dio.copa.catar.features

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.dio.copa.catar.core.BaseViewModel
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.usecase.DisableNotificationUseCase
import me.dio.copa.catar.domain.usecase.EnableNotificationUseCase
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.remote.NotFoundException
import me.dio.copa.catar.remote.UnexpectedException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val disableNotificationUseCase: DisableNotificationUseCase,
    private val enableNotificationUseCase: EnableNotificationUseCase,
) : BaseViewModel<MainUiState, MainUiAction>(MainUiState()) {

    init {
        fetchMatches()
    }

    // busca e devolve as matches (partidas) no viewModel, consumindo o GetMatchesUseCase
    private fun fetchMatches() = viewModelScope.launch { // 'viewModelScope' é p o uso de Coroutines
        getMatchesUseCase()
            .flowOn(Dispatchers.Main)
            .catch { // captura qualquer excessão que houver
                when(it) {
                    is NotFoundException ->
                        sendAction(MainUiAction.MatchesNotFound(it.message ?: "Erro sem mensagem"))
                    is UnexpectedException ->
                        sendAction(MainUiAction.Unexpected)
                }
            }.collect { matches ->
                setState {
                    copy(matches = matches)
                }
            }
    }

    // fun que troca de habilitada p desabilitada e vice-versa a notificação de início do jogo
    fun toggleNotification(match: Match) {
        viewModelScope.launch {
            runCatching { // permite que a aplicação não quebre/feche sozinha caso aconteça algum erro
                withContext(Dispatchers.Main) {
                    // se estiver habilitada, desabilita ...
                    val action = if (match.notificationEnabled) {
                        disableNotificationUseCase(match.id)
                        MainUiAction.DisableNotification(match)
                        // se não, se estiver desabilitada, habilita
                    } else {
                        enableNotificationUseCase(match.id)
                        MainUiAction.EnableNotification(match)
                    }

                    sendAction(action)
                }
            }
        }
    }
}

// mostra a lista - sendo que, o estado inicial é uma lista vazia -> emptyList()
data class MainUiState(
    val matches: List<MatchDomain> = emptyList()
)

// classe selada, com as coisas que podem acontecer como: erros, notificação habilitada ou desabilitada...
sealed interface MainUiAction {
    object Unexpected: MainUiAction
    data class MatchesNotFound(val message: String) : MainUiAction
    data class EnableNotification(val match: MatchDomain) : MainUiAction
    data class DisableNotification(val match: MatchDomain) : MainUiAction
}
