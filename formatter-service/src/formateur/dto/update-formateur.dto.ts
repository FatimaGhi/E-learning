import { IsEmail, IsOptional, IsString, MinLength, IsDateString, IsBoolean } from 'class-validator';

export class UpdateFormateurDto {
  @IsOptional()
  @IsString()
  firstName?: string;

  @IsOptional()
  @IsString()
  lastName?: string;

  @IsOptional()
  @IsEmail()
  email?: string;


  @IsOptional()
  @IsString()
  username?: string;

  @IsOptional()
  @IsDateString()
  dateOfBirth?: string;

  @IsOptional()
  @IsBoolean()
  isActive?: boolean;
}