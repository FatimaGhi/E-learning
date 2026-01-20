import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { FormateurController } from './formateur.controller';
import { FormateurService } from './formateur.service';
import { Formateur, FormateurSchema } from './schemas/formateur.schema';
import { AuthServiceClient } from 'src/common/clients/auth-service.client';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Formateur.name, schema: FormateurSchema }
    ]),
  ],
  controllers: [FormateurController],
  providers: [FormateurService, AuthServiceClient],
  exports: [FormateurService],
})
export class FormateurModule {}