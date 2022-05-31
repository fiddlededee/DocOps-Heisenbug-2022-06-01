const login = 'ivanov1981'
const email = 'ivanov1981@bar.foo'
const fullName = 'Ivan Ivanov'

/* @ts-ignore */
outputUserInfo(email, fullName)

function outputUserInfo(login: string, email: string, fullName: string) {
  console.log(`login: ${login}\nemail: ${email}\nfull name: ${fullName}`)
}

