import { login, logout } from '@/api/user'

const state = {
  token: localStorage.getItem('token'),
  userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
    localStorage.setItem('userInfo', JSON.stringify(userInfo))
  },
  CLEAR_USER: (state) => {
    state.token = ''
    state.userInfo = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }
}

const actions = {
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          const { data } = response
          commit('SET_TOKEN', data.token)
          commit('SET_USER_INFO', data.userInfo)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  logout({ commit }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        commit('CLEAR_USER')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 