
import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type FormateurDocument = Formateur & Document;

@Schema({ timestamps: true })
export class Formateur {
  @Prop({ required: true })
  firstName: string;

  @Prop({ required: true })
  lastName: string;

  @Prop({ required: true, unique: true })
  email: string;

  @Prop({ required: true, unique: true })
  username: string;

  @Prop({ required: true })
  dateOfBirth: Date;

  @Prop({ default: 0 })
  likesCount: number; // Number of likes this formateur has received

  @Prop({ default: true })
  isActive: boolean;

  @Prop()
  createdAt?: Date;

  @Prop()
  updatedAt?: Date;

   @Prop()
   keycloakUserId: string;
}

export const FormateurSchema = SchemaFactory.createForClass(Formateur);