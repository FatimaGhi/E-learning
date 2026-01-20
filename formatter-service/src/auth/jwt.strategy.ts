import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import * as jwksClient from 'jwks-rsa';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor() {
    console.log(' JwtStrategy Initialized!');
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKeyProvider: jwksClient.passportJwtSecret({
        cache: true,
        rateLimit: true,
        jwksRequestsPerMinute: 5,
        jwksUri: `${process.env.KEYCLOAK_URL || 'http://localhost:8080'}/realms/${process.env.KEYCLOAK_REALM || 'e-learning-realm'}/protocol/openid-connect/certs`,
      }),
      issuer: `${process.env.KEYCLOAK_URL || 'http://localhost:8080'}/realms/${process.env.KEYCLOAK_REALM || 'e-learning-realm'}`,
      algorithms: ['RS256'],
    });
  }

  async validate(payload: any) {
    console.log('============================================');
    console.log(' JWT VALIDATION START');
    console.log('============================================');
    console.log(' Payload:', JSON.stringify(payload, null, 2));
    console.log(' User ID:', payload.sub);
    console.log(' Email:', payload.email);
    console.log(' Roles:', payload.realm_access?.roles);
    console.log('============================================');

    if (!payload) {
      console.error(' Invalid token payload');
      throw new UnauthorizedException('Invalid token payload');
    }

    if (!payload.realm_access || !payload.realm_access.roles) {
      console.error(' No roles found in token');
      throw new UnauthorizedException('No roles found in token');
    }

    console.log(' JWT Validation successful');
    console.log('============================================');

    return {
      userId: payload.sub,
      username: payload.preferred_username,
      email: payload.email,
      name: payload.name,
      givenName: payload.given_name,
      familyName: payload.family_name,
      realm_access: payload.realm_access,
      resource_access: payload.resource_access,
    };
  }
}