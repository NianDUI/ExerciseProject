import JSEncrypt from 'jsencrypt'
import Cookies from 'js-cookie'
import { ElMessageBox, ElMessage } from 'element-plus'

const PUBLIC_KEY = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg2Zsf4SrrGAJMSrfsjNJqyk9KxXNqcZUGttaIp/rsBJYyXidXR9r5NbYZ5ahqmNjSlWWxYmMfkPpe5nUFQyxsCAwEAAQ=='

function encryptPlainToken (plain) {
  const enc = new JSEncrypt()
  enc.setPublicKey('-----BEGIN PUBLIC KEY-----' + PUBLIC_KEY + '-----END PUBLIC KEY-----')
  return enc.encrypt(plain)
}

function saveToken (encrypted) {
  sessionStorage.setItem('token', encrypted)
  Cookies.set('token', encrypted)
}

export async function setTokenByPrompt () {
  try {
    const { value } = await ElMessageBox.prompt('请输入 Token', '设置 Token', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入 token',
      inputValidator: (val) => {
        if (!val || !val.trim()) return 'Token 不能为空'
        return true
      }
    })
    const encrypted = encryptPlainToken(value.trim())
    if (!encrypted) {
      ElMessage.error('Token 加密失败')
      return null
    }
    saveToken(encrypted)
    ElMessage.success('Token 更新成功')
    return encrypted
  } catch {
    // 用户点了取消
    return null
  }
}

export function getToken () {
  let token = Cookies.get('token')
  if (!token) {
    token = sessionStorage.getItem('token')
  }
  if (!token) {
    setTokenByPrompt()
    // 返回空字符串，等用户确认后下次请求生效
    return ''
  }
  return token
}

export function clearToken () {
  sessionStorage.removeItem('token')
  Cookies.remove('token')
}
