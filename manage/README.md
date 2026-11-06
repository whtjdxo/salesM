# Manage 애플리케이션

## 필수 TOTP 로그인

배포 후 모든 활성 사용자는 첫 로그인 때 인증 앱을 등록해야 합니다. QR 코드는 애플리케이션 내부에서 생성하며, Google Authenticator 또는 TOTP 호환 앱을 사용할 수 있습니다.

### 배포 순서

1. 데이터베이스를 백업합니다.
2. MySQL에 [`db/migration/V20261031_01__add_totp_to_tb_user.sql`](db/migration/V20261031_01__add_totp_to_tb_user.sql)을 적용합니다.
3. 32바이트 난수를 생성하고 Base64로 인코딩한 값을 애플리케이션 프로세스의 `TOTP_ENCRYPTION_KEY` 환경 변수로 설정합니다. 예: `openssl rand -base64 32`. 키를 소스 코드에 저장하지 말고, 재시작 후에도 동일하게 유지되도록 안전하게 보관·백업합니다.
4. 이 폴더에서 `.\mvnw.cmd clean package`를 실행하고 `target\ROOT.war`를 배포합니다.

키가 없거나 Base64로 인코딩된 32바이트 값이 아니면 애플리케이션이 시작되지 않습니다. TOTP 비밀키는 AES-GCM으로 암호화해 저장합니다. 키를 분실하거나 변경하면 등록된 모든 인증 앱을 다시 등록해야 합니다.

#### Linux systemd 서버 환경 설정

`application.properties`에서 `app.totp.encryption-key`는 `TOTP_ENCRYPTION_KEY` 환경 변수를 읽습니다. 운영 서버에서는 키를 properties 파일이나 저장소에 넣지 말고, systemd 서비스의 환경 변수로 설정합니다.

1. 서버에서 32바이트 난수를 Base64로 인코딩해 전용 환경 파일에 한 번만 생성합니다.

   ```sh
   sudo install -d -m 700 /etc/salesm
   sudo sh -c 'umask 077; printf "TOTP_ENCRYPTION_KEY=%s\n" "$(openssl rand -base64 32 | tr -d "\n")" > /etc/salesm/totp.env'
   sudo chmod 600 /etc/salesm/totp.env
   ```

2. 애플리케이션 systemd 서비스에 환경 파일을 연결합니다. `salesm.service`는 실제 서비스 이름으로 바꿉니다.

   ```sh
   sudo systemctl edit salesm.service
   ```

   편집기에 다음 설정을 추가하고 저장합니다.

   ```ini
   [Service]
   EnvironmentFile=/etc/salesm/totp.env
   ```

3. 설정을 반영하고 서비스를 재시작한 뒤 시작 로그를 확인합니다.

   ```sh
   sudo systemctl daemon-reload
   sudo systemctl restart salesm.service
   sudo systemctl status salesm.service
   sudo journalctl -u salesm.service -n 100 --no-pager
   ```

환경 파일은 재생성하거나 키 값을 바꾸지 마세요. 이 키는 TOTP 비밀키 암복호화에 사용되므로 동일한 키를 안전하게 백업해야 합니다. 유출된 키를 교체해야 한다면 기존 TOTP 등록 정보를 다시 등록해야 합니다.

인증 등록 대기 세션은 5분 후 만료됩니다. 인증 코드를 5회 틀리면 5분 동안 추가 인증이 제한되며, 한 번 사용한 시간 구간의 코드는 재사용할 수 없습니다.

### 기기 분실 시 초기화

시스템 관리자는 **사용자 관리** 화면에서 다른 사용자의 2단계 인증을 초기화할 수 있습니다. 해당 사용자는 다음 로그인 때 다시 등록해야 합니다. 초기화 전에 이미 로그인한 세션은 자동 종료되지 않으므로 로그아웃하거나 세션이 만료될 때까지 유효합니다.

유일한 시스템 관리자가 기기를 분실한 경우 DBA가 해당 사용자의 `TOTP_SECRET_ENC`를 `NULL`로, `TOTP_LAST_COUNTER`를 `-1`로 설정하고 `TOTP_FAILED_ATTEMPTS`, `TOTP_LOCKED_UNTIL`을 초기화해야 합니다.
