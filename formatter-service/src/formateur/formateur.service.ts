
import { Injectable, NotFoundException, ConflictException, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Formateur, FormateurDocument } from './schemas/formateur.schema';
import { CreateFormateurDto } from './dto/create-formateur.dto';
import { UpdateFormateurDto } from './dto/update-formateur.dto';
import { AuthServiceClient } from 'src/common/clients/auth-service.client';

@Injectable()
export class FormateurService {
 private readonly logger = new Logger(FormateurService.name);
  [x: string]: any;
  constructor(
    @InjectModel(Formateur.name)
    private formateurModel: Model<FormateurDocument>,
    private authServiceClient: AuthServiceClient,
  ) {}

  async create(createDto: CreateFormateurDto ,authToken: string): Promise<Formateur> {

    this.logger.log(`Creating formateur: ${createDto.email}`);
    
    // Check if email or username already exists
    const existingEmail = await this.formateurModel.findOne({ email: createDto.email });
    if (existingEmail) {
      throw new ConflictException('Email already exists');
    }

    const existingUsername = await this.formateurModel.findOne({ username: createDto.username });
    if (existingUsername) {
      throw new ConflictException('Username already exists');
    }

    // new code to create user in auth service
    
    try {
      // Step 1: Call Auth Service to create user in Keycloak
      this.logger.log('📡 Calling Auth Service...');
      const authResponse = await this.authServiceClient.registerUser(
        {
          firstName: createDto.firstName,
          lastName: createDto.lastName,
          email: createDto.email,
          username: createDto.username,
          password: createDto.password,
        },
        authToken,  //  Pass admin token
      );

      this.logger.log(`User created in Keycloak: ${authResponse.data.userId}`);

       // Step 2: Save in MongoDB (NO password)
      const formateur = new this.formateurModel({
        firstName: createDto.firstName,
        lastName: createDto.lastName,
        email: createDto.email,
        username: createDto.username,
        dateOfBirth: createDto.dateOfBirth,
        keycloakUserId: authResponse.data.userId,
      });

      const saved = await formateur.save();
      this.logger.log(` Formateur saved in MongoDB: ${saved._id}`);

      return saved;
    } catch (error) {
      this.logger.error(` Failed to create formateur: ${error.message}`);
      throw error;
    }
  }

  async findAll(): Promise<Formateur[]> {
    return this.formateurModel.find().select('-password').exec();
  }

  async findById(id: string): Promise<Formateur> {
    const formateur = await this.formateurModel.findById(id).select('-password').exec();
    
    if (!formateur) {
      throw new NotFoundException(`Formateur with ID ${id} not found`);
    }

    return formateur;
  }

  async findByUsername(username: string): Promise<Formateur> {
    const formateur = await this.formateurModel
      .findOne({ username })
      .select('-password')
      .exec();

    if (!formateur) {
      throw new NotFoundException(`Formateur with username ${username} not found`);
    }

    return formateur;
  }

  async update(id: string, updateDto: UpdateFormateurDto): Promise<Formateur> {

    // Check for email uniqueness if being updated
    if (updateDto.email) {
      const existingEmail = await this.formateurModel.findOne({
        email: updateDto.email,
        _id: { $ne: id },
      });
      if (existingEmail) {
        throw new ConflictException('Email already exists');
      }
    }

    // Check for username uniqueness if being updated
    if (updateDto.username) {
      const existingUsername = await this.formateurModel.findOne({
        username: updateDto.username,
        _id: { $ne: id },
      });
      if (existingUsername) {
        throw new ConflictException('Username already exists');
      }
    }

    const formateur = await this.formateurModel
      .findByIdAndUpdate(id, updateDto, { new: true })
      .select('-password')
      .exec();

    if (!formateur) {
      throw new NotFoundException(`Formateur with ID ${id} not found`);
    }

    return formateur;
  }

  async delete(id: string): Promise<void> {
    const result = await this.formateurModel.findByIdAndDelete(id).exec();

    if (!result) {
      throw new NotFoundException(`Formateur with ID ${id} not found`);
    }
  }

  async incrementLikes(id: string): Promise<Formateur> {
    const formateur = await this.formateurModel
      .findByIdAndUpdate(
        id,
        { $inc: { likesCount: 1 } },
        { new: true }
      )
      .select('-password')
      .exec();

    if (!formateur) {
      throw new NotFoundException(`Formateur with ID ${id} not found`);
    }

    return formateur;
  }
}