import { Injectable, Logger, HttpException, HttpStatus } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';

export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
}

export interface AuthServiceResponse {
  status: string;
  data: {
    userId: string;
    email?: string;
    username?: string;
  };
}

@Injectable()
export class AuthServiceClient {
  private readonly logger = new Logger(AuthServiceClient.name);
  private readonly authServiceUrl: string;

  constructor(private configService: ConfigService) {
    this.authServiceUrl = this.configService.get<string>(
      'AUTH_SERVICE_URL',
      'http://localhost:9001/api/auth',
    );
    this.logger.log(` ********** Auth Service URL: ${this.authServiceUrl}`);
  }

  /**
   * Register a new user in Keycloak via Auth Service
   * @param request User data
   * @param authorizationHeader Full "Bearer TOKEN" from admin
   */
  async registerUser(
    request: CreateUserRequest,
    authorizationHeader: string,
  ): Promise<AuthServiceResponse> {
    try {
      this.logger.log(` Calling Auth Service to register: ${request.email}`);

      const response = await fetch(`${this.authServiceUrl}/formattor/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': authorizationHeader,
        },
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({ 
          message: 'Auth Service error' 
        }));
        
        this.logger.error(` Auth Service returned ${response.status}`);
        this.logger.error(`Error: ${JSON.stringify(errorData)}`);
        
        throw new HttpException(
          errorData.message || 'Failed to register user in Keycloak',
          response.status
        );
      }

      const data: AuthServiceResponse = await response.json();
      this.logger.log(` User registered successfully: ${data.data.userId}`);

      return data;
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }

      this.logger.error(` Failed to call Auth Service: ${error.message}`);
      throw new HttpException(
        'Failed to communicate with Auth Service',
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }
}