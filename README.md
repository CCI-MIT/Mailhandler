# Mailhandler

[![Build Status](https://travis-ci.org/CCI-MIT/Mailhandler.svg?branch=master)](https://travis-ci.org/CCI-MIT/Mailhandler)

This script allows you to set up a simple email forwarding server using [SendGrid's Inbound Parse](https://sendgrid.com/docs/User_Guide/Settings/parse.html). You can configure several domains and user names, each forwarding to several emails addresses.

## Usage

You can configure the mailhandler by placing a `application.yml` file in the same directory as your `mailhandler.jar`.

### Configure SMTP server

Mailhandler uses an SMTP server of your choice to send the messages. The default is `localhost:8025`. You can configure it as follows:

```yml
mail:
  smtp:
    host: localhost
    post: 8025
    user: myuser
    pass: mypass
    transport: TLS
```

You domains can also be configured in the same file. To forward emails received at `admin@example.com` to `admin1@example.com` and `admin2@example.com`, you can use the following configuration:

```yml
mail:
  domains:
    - domain: example.com
      mappings: 
        - admin:
          - admin1@example.com
          - admin1@example.com
```

## Testing

You can test the application by creating a local tunning using [ngrok](https://ngrok.com/).

1. Install ngrok - on macOS with homebrew:
  ```bash
  brew cask install ngrok
  ```

2. Create a tunnel to your local instance (by default it's on port `8181`):
  ```bash
  ngrok http 8181
  ```
3. In the SendGrid dashboard, go to Settings -> Inbound Parse and configure your domain to point the url you received from ngrok, e.g. `https://a93b4408.ngrok.io`. Make sure you check "Spam check" but not "Send raw". Make sure your domain has the MX record configured to point to SendGrid's servers.

4. Configure your domain in `src/main/resources/application.yml`

5. Send an email to the email you configured, ngrok will allow you to replay the request as often as you want through the [web interface running on port 4040](http://localhost:4040).
