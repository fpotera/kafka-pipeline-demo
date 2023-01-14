# Copyright 2023 Florin Potera
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import http.client, urllib.parse, json

USER = 'admin'
PASSWORD = 'admin'
GRANT_TYPE = 'password'
CLIENT_ID = 'admin-cli'

KEYCLOAK_HOST_PORT = 'idp:8080'
TOKEN_PATH = '/realms/master/protocol/openid-connect/token'
CLIENTS_PATH = '/admin/realms/master/clients'

TEST_CLIENT_FILE = 'test-client.json'


def main():
    conn = http.client.HTTPConnection(KEYCLOAK_HOST_PORT)
    access_token = get_access_token(conn)

    print('access_token:', access_token)

    create_client(conn, access_token)
    get_clients(conn, access_token)

    conn.close()


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


def create_client(conn, access_token):
    headers = {'Content-type': 'application/json',
               'Accept': 'application/json',
               'Authorization': 'bearer ' + access_token}

    with open(TEST_CLIENT_FILE) as body:
        conn.request("POST", CLIENTS_PATH, body, headers)
        response = conn.getresponse()
        print('*** realm creation result:', response.status, response.reason)
        response.read()


def get_clients(conn, access_token):
    headers = {'Content-type': 'application/json',
               'Accept': 'application/json',
               'Authorization': 'bearer ' + access_token}

    conn.request("GET", CLIENTS_PATH + '?clientId=TestClient', None, headers)
    response = conn.getresponse()
    print('*** get clients result:', response.status, response.reason)

    data = response.read()
    print(data)


if __name__ == "__main__":
    main()
