import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Headers,
  UseGuards,
  HttpException,
  Patch,
  Req,
} from '@nestjs/common';
import { Request } from '@nestjs/common';
import { FormateurService } from './formateur.service';
import { CreateFormateurDto } from './dto/create-formateur.dto';
import { UpdateFormateurDto } from './dto/update-formateur.dto';
import { GlobalResponse } from '../common/responses/global-response';
import { Roles } from '../common/decorators/roles.decorator';
import { RolesGuard } from '../common/guards/roles.guard';
import { AuthGuard } from '@nestjs/passport';


@Controller('formateurs')
export class FormateurController {
  constructor(private readonly formateurService: FormateurService) {}


 @Get('test-no-auth')
  async testNoAuth() {
    console.log('============================================');
    console.log(' TEST NO AUTH - Service is alive!');
    console.log('============================================');
    
    return GlobalResponse.success({
      message: 'Service is working! No auth required for this endpoint',
      timestamp: new Date().toISOString(),
    });
  }
     // Test endpoint to debug authentication
  @Get('test-auth')
  @UseGuards(AuthGuard('jwt'), RolesGuard)
  @Roles('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_FORMATEUR')
  async testAuth(@Req() req: Request & { user: any }) {
    console.log('============================================');
    console.log(' TEST AUTH ENDPOINT');
    console.log('============================================');
    console.log('User from request:', JSON.stringify(req.user, null, 2));
    console.log('============================================');
    
    return GlobalResponse.success({
      message: 'Authentication successful!',
      user: req.user,
    });
  }




    // Only ADMIN can create formateurs

  @Post()
  @Roles('ROLE_ADMIN')
  async create(@Body() createDto: CreateFormateurDto,
  @Headers('authorization') authorization: string,
   @Req() req: Request & { user: any }) {
    console.log(' CREATE FORMATEUR by:', req.user?.email);
    console.log(' Forwarding to Auth Service...');
    
    // Pass token to Auth Service
    const formateur = await this.formateurService.create(createDto, authorization);
    return GlobalResponse.success(formateur);
  }





  // ADMIN, STUDENT, and FORMATEUR can view all formateurs
  @Get()
  @Roles('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_FORMATEUR')
  async findAll() {
    try {
      const formateurs = await this.formateurService.findAll();
      return GlobalResponse.success(formateurs);
    } catch (error) {
      return GlobalResponse.errorMessage(error.message || 'Failed to fetch formateurs');
    }
  }

  // ADMIN, STUDENT, and FORMATEUR can view a formateur by ID
  @Get(':id')
  @Roles('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_FORMATEUR')
  async findById(@Param('id') id: string) {
    try {
      const formateur = await this.formateurService.findById(id);
      return GlobalResponse.success(formateur);
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      return GlobalResponse.errorMessage(error.message || 'Failed to fetch formateur');
    }
  }

  // Get formateur by username
  @Get('username/:username')
  @Roles('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_FORMATEUR')
  async findByUsername(@Param('username') username: string) {
    try {
      const formateur = await this.formateurService.findByUsername(username);
      return GlobalResponse.success(formateur);
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      return GlobalResponse.errorMessage(error.message || 'Failed to fetch formateur');
    }
  }

  // Only ADMIN can delete formateurs
  @Delete(':id')
  @Roles('ROLE_ADMIN')
  async delete(@Param('id') id: string) {
    try {
      await this.formateurService.delete(id);
      return GlobalResponse.success({ message: 'Formateur deleted successfully' });
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      return GlobalResponse.errorMessage(error.message || 'Failed to delete formateur');
    }
  }

  // ADMIN can update any formateur, FORMATEUR can update their own profile
  @Put(':id')
  @Roles('ROLE_ADMIN', 'ROLE_FORMATEUR')
  async update(@Param('id') id: string, @Body() updateDto: UpdateFormateurDto) {
    try {
      const formateur = await this.formateurService.update(id, updateDto);
      return GlobalResponse.success(formateur);
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      return GlobalResponse.errorMessage(error.message || 'Failed to update formateur');
    }
  }

  // Increment likes for a formateur - accessible by students
  @Patch(':id/like')
  @Roles('ROLE_STUDENT', 'ROLE_ADMIN')
  async incrementLikes(@Param('id') id: string) {
    try {
      const formateur = await this.formateurService.incrementLikes(id);
      return GlobalResponse.success(formateur);
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      return GlobalResponse.errorMessage(error.message || 'Failed to like formateur');
    }
  }



  
}
