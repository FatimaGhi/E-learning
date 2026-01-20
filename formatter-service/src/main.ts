import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';


import { ValidationPipe } from '@nestjs/common';
import { ConsulService } from './config/consul.config';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Enable CORS
  app.enableCors();

  // Enable validation pipes
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
    }),
  );

  // Health check endpoint


  const port = process.env.SERVICE_PORT || 3002;
 await app.listen(port, '0.0.0.0');

  console.log(`Formateur Service is running on: http://127.0.0.1:${port}`);

  // Register with Consul
  const consulService = new ConsulService();
  try {
    await consulService.registerService();
  } catch (error) {
    console.error('Failed to register with Consul, continuing without service discovery');
  }

  // Handle graceful shutdown
  process.on('SIGINT', async () => {
    console.log('Received SIGINT, deregistering from Consul...');
    await consulService.deregisterService();
    await app.close();
    process.exit(0);
  });

  process.on('SIGTERM', async () => {
    console.log('Received SIGTERM, deregistering from Consul...');
    await consulService.deregisterService();
    await app.close();
    process.exit(0);
  });


  app.enableCors();
}

bootstrap();