const getters = {
  sidebar: state => state.app.sidebar,

  token: state => state.account.token,
  accountId: state => state.account.accountId,
  email: state => state.account.email,
  nickname: state => state.account.nickname,
}
export default getters
