package cc.rits.membership.console.iam.usecase

import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME

/**
 * LoginUseCaseの単体テスト
 */
class LoginUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    LoginUseCase sut

    def "handle: ログインに成功するとセッションにユーザ情報を記録する"() {
        given:
        final requestBody = RandomHelper.mock(LoginRequest)

        when:
        this.sut.handle(requestBody)

        then:
        noExceptionThrown()
        1 * this.authenticationProvider.authenticate(_) >> new UsernamePasswordAuthenticationToken(null, null, null)
        1 * this.httpSession.setAttribute(PRINCIPAL_NAME_INDEX_NAME, _);
        1 * this.httpServletRequest.changeSessionId()
    }

}