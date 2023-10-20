package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

// tenho que ir no repository (onde vai ter a interface das partidas)...
// habilitar as notificações da listagem
// cada notificação tem uma identificação -> (id: String)

class EnableNotificationUseCase @Inject constructor(
    private val repository: MatchesRepository
) {
    suspend operator fun invoke(id: String) {
        repository.enableNotificationFor(id)
    }
}
