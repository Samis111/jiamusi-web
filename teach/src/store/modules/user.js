import { login, logout } from '@/api/user'

const state = {
  token: localStorage.getItem('token'),
  userInfo: {
    role: '',
    username: ''
  }
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  CLEAR_USER: (state) => {
    state.token = ''
    state.userInfo = {
      role: '',
      username: ''
    }
    localStorage.removeItem('token')
  }
}

const actions = {
  login({ commit }, userInfo) {
    const { username, password, role } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          commit('SET_USER_INFO', {
            username,
            role
          })
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