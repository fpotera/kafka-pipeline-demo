import http.client, urllib.parse, json

USER = 'admin'
PASSWORD = 'admin'
GRANT_TYPE = 'password'
CLIENT_ID = 'admin-cli'

KEYCLOAK_HOST_PORT = 'idp:8080'
TOKEN_PATH = '/realms/master/protocol/openid-connect/token'
REALMS_PATH = '/admin/realms'

TEST_REALM_FILE = 'test-realm.json'

conn = http.client.HTTPConnection(KEYCLOAK_HOST_PORT)

def get_access_token(conn):
    params = urllib.parse.urlencode({'client_id': CLIENT_ID, 
                                     'username': USER, 
                                     'password': PASSWORD,
                                     'grant_type': GRANT_TYPE})
    headers = {'Content-type': 'application/x-www-form-urlencoded',
               'Accept': 'application/json'}
    
    conn.request("POST", TOKEN_PATH, params, headers)
    response = conn.getresponse()
    print('*** access token creation result:', response.status, response.reason)

    data = response.read()
    
    return json.loads(data)['access_token']

def create_realm(conn, access_token):
    headers = {'Content-type': 'application/json',
               'Accept': 'application/json',
               'Authorization': 'bearer '+access_token}

    with open(TEST_REALM_FILE) as body:
        conn.request("POST", REALMS_PATH, body, headers)
        response = conn.getresponse()
        
    print('*** realm creation result:', response.status,  response.reason)   

def create_client(conn, access_token):
    headers = {'Content-type': 'application/json',
               'Accept': 'application/json',
               'Authorization': 'bearer '+access_token}    

access_token = get_access_token(conn)

print('access_token:', access_token)

create_realm(conn, access_token)

conn.close()
