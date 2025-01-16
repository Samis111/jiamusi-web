import { login, logout } from '@/api/user'

const state = {
  userInfo: {
    role: '',
    username: ''
  }
}

const mutations = {
  
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
    localStorage.setItem('userInfo', userInfo)
  },
  CLEAR_USER: (state) => {
    state.userInfo = {
      role: '',
      username: ''
    }
    localStorage.removeItem('userInfo')
  }
}

const actions = {
  login({ commit }, userInfo) {
    const { username, password, role } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          commit('SET_USER_INFO',response.data)
          return resolve(response)
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