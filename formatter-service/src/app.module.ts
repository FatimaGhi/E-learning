import { Module } from '@nestjs/common';

import { ConfigModule } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { PassportModule } from '@nestjs/passport';
import { FormateurModule } from './formateur/formateur.module';
import { APP_GUARD } from '@nestjs/core';
import { RolesGuard } from './common/guards/roles.guard';
import { JwtStrategy } from './auth/jwt.strategy';
import { HealthModule } from './health/health.module';

@Module({
  imports: [
     ConfigModule.forRoot({
      isGlobal: true,
    }),
    MongooseModule.forRoot(
      process.env.MONGODB_URI || 'mongodb://localhost:27017/formateur-db'
    ),
    PassportModule.register({ defaultStrategy: 'jwt' }),
    FormateurModule,
    HealthModule,
  ],
  controllers: [],
  providers: [JwtStrategy,
    // {
    //   provide: APP_GUARD,
    //   useClass: RolesGuard,
      
    // },
  ],
})
export class AppModule {}
