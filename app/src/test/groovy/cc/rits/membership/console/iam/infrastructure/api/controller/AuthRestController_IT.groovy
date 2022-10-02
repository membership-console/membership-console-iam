package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest
import org.springframework.http.HttpStatus

/**
 * AuthRestControllerの統合テスト
 */
class AuthRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api"
    static final String LOGIN_PATH = BASE_PATH + "/login"
    static final String LOGOUT_PATH = BASE_PATH + "/logout"

    def "ログインAPI: 正常系 ログインに成功するとセッションにユーザ情報が記録される"() {
        given:
        final user = this.login()
        final oldSessionId = this.session.getId()

        final requestBody = LoginRequest.builder()
            .email(user.email)
            .password(user.password)
            .build()

        when:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        !this.session.isInvalid()
        // セッションIDが変更されていることを確認 (セッションジャック対策)
        oldSessionId != this.session.getId()
    }

    def "ログインAPI: 異常系 メールアドレスかパスワードが間違っている場合、401エラー"() {
        given:
        final user = this.login()

        when: "メールアドレスが間違っている"
        def requestBody = LoginRequest.builder()
            .email(user.email + "aaa")
            .password(user.password)
            .build()
        def request = this.postRequest(LOGIN_PATH, requestBody)

        then:
        this.execute(request, new UnauthorizedException(ErrorCode.INCORRECT_EMAIL_OR_PASSWORD))

        when: "パスワードが間違っている"
        requestBody = LoginRequest.builder()
            .email(user.email)
            .password(user.password + "aaa")
            .build()
        request = this.postRequest(LOGIN_PATH, requestBody)

        then:
        this.execute(request, new UnauthorizedException(ErrorCode.INCORRECT_EMAIL_OR_PASSWORD))
    }

    def "ログアウトAPI: 正常系 ログアウトするとセッションが廃棄される"() {
        given:
        this.login()

        when:
        final request = this.postRequest(LOGOUT_PATH)
        this.execute(request, HttpStatus.OK)

        then:
        this.session.isInvalid()
    }

    def "ログアウトAPI: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.postRequest(LOGOUT_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
